import './styles/style.css';

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let isTouchDevice = 'ontouchstart' in window || navigator.maxTouchPoints;
let touchDownEvent = isTouchDevice ? 'touchstart' : 'mousedown';
let touchReleaseEvent = isTouchDevice ? 'touchend' : 'mouseup';

const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
let audioBuffer = null;

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
    const {target} = ev;

    try {
        const text = findForDatasetValueIncludingAncestors(target, 'key');
        await api.keyboard.inputText(text, eventType);
    } catch (error) {
        console.error(error);
    }
}

const bindKeys = () => {

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
                await getKeyAndSendRequest(ev, Event.RELEASED);
            });
        })
}

document.addEventListener('DOMContentLoaded', () => {
    loadKeyReleaseAudio();
    bindKeys();
});


