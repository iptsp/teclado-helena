import './styles/style.css';

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let isTouchDevice = 'ontouchstart' in window || navigator.maxTouchPoints;
let touchDownEvent = isTouchDevice ? 'touchstart' : 'mousedown';
let touchReleaseEvent = isTouchDevice ? 'touchend' : 'mouseup';

const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
let audioBuffer = null;

let isLongPressed = false;

const timeLimitLongPress = 500;

const loadKeyReleaseAudio = async () => {
    try {
        const response = await fetch("./audio/key-release.mp3");
        const buffer = await response.arrayBuffer();
        audioBuffer = await audioCtx.decodeAudioData(buffer);
    } catch (error) {
        console.error(error);
    }
}

const Event = {
    PRESSED: "PRESSED",
    RELEASED: "RELEASED"
}

const inputText = async (text, event) => {
    const request = {
        method: 'POST',
        body: JSON.stringify({text, event}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/keyboards/inputs`, request);
    return await response.json();
}

const api = {
    keyboard: {
        inputText
    }
}

const playKeyReleaseAudio = async () => {
    try {
        const releaseAudio = await audioCtx.createBufferSource();
        releaseAudio.buffer = audioBuffer;
        releaseAudio.connect(audioCtx.destination);
        releaseAudio.start(0);
    } catch (error) {
        console.error(error);
    }
}

const vibrateOnKeyRelease = async () => {
    if (!navigator.vibrate) {
        return;
    }
    return navigator.vibrate(100);
}

const keyReleaseAllFeedback = async () => {
    await playKeyReleaseAudio();
    await vibrateOnKeyRelease();
}

const toggleLongPressClick = async (element) => {
    element.parentElement.querySelectorAll('[data-long-pressable="true"]')
        .forEach((element) => {
            if (!isLongPressed) {
                element.classList.add('active');
            } else {
                element.classList.remove('active');
            }
        });

    isLongPressed = !isLongPressed;
}

const deactivateLongPress = async (element) =>{
    if(isLongPressed){
        await toggleLongPressClick(element);
    }
}

const findForDatasetValueIncludingAncestors = (el, attr) => {

    if (el.target && el.target.dataset[attr]) {
        return el.target.dataset[attr];
    }

    if (el.parentElement && el.parentElement.dataset[attr]) {
        return el.parentElement.dataset[attr];
    }

    if (el.parentElement.parentElement && el.parentElement.parentElement.dataset[attr]) {
        return el.parentElement.parentElement.dataset[attr];
    }

    throw new Error(`No dataset value found for element ${el} with attr ${attr}`);
}

const getKeyAndSendRequest = async (ev, eventType) => {

    const calcText = () => {
        if (isLongPressed){
            return findForDatasetValueIncludingAncestors(target,'longPress')
        }
        return findForDatasetValueIncludingAncestors(target, 'key');
    }

    const {target} = ev;

    try {
        const text = calcText();
        await api.keyboard.inputText(text, eventType);
    } catch (error) {
        console.error(error);
    } finally {
        await deactivateLongPress(target.parentElement);
    }
}

const bindKeys = () => {

    document.querySelectorAll('[data-long-pressable="true"]')
        .forEach((element) =>{

            var pressTimer;

            element.addEventListener(touchDownEvent, async(event)=>{
                pressTimer = setTimeout(async () => {
                    isLongPressed = true;
                    await getKeyAndSendRequest(event, Event.PRESSED);
                    await toggleLongPressClick(element);
                }, timeLimitLongPress);
            });

            element.addEventListener(touchReleaseEvent, async(event) => {
                clearTimeout(pressTimer);
                await getKeyAndSendRequest(event, Event.RELEASED);
                isLongPressed = false;
            })
        });

    document.querySelectorAll('[data-feedback="audio|vibrate"]')
        .forEach((element) => {

            element.addEventListener(touchReleaseEvent, async (_) => {
                await keyReleaseAllFeedback();
            });
        });

    document.querySelectorAll('[data-key]')
        .forEach((element) => {

            element.addEventListener(touchDownEvent, async (ev) => {
                await getKeyAndSendRequest(ev, Event.PRESSED);
            });

            element.addEventListener(touchReleaseEvent, async (ev) => {
                await deactivateLongPress(element);
                await getKeyAndSendRequest(ev, Event.RELEASED);
            });
        });
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadKeyReleaseAudio();
    bindKeys();
});


