import './styles/style.css';

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

const isTouchDevice = 'ontouchstart' in window || navigator.maxTouchPoints;
const touchDownEvent = isTouchDevice ? 'touchstart' : 'mousedown';
const touchReleaseEvent = isTouchDevice ? 'touchend' : 'mouseup';
const touchMoveEvent = isTouchDevice ? 'touchmove' : 'mousemove';
let isMouseEnabled = false;

const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
let audioBuffer = null;

let isLongPressed = false;

const timeLimitLongPress = 500;

var pressTimer;

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

const leftClick = async () => {
    const request = {
        method: 'POST',
        body: JSON.stringify({}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/buttons/left/events/click`, request);
    return await response.json();
}

const rightClick = async () => {
    const request = {
        method: 'POST',
        body: {},
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/buttons/right/events/click`, request);
    return await response.json();
}

const scrollUp = async (units) => {
    const request = {
        method: 'POST',
        body: JSON.stringify({units}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/scrolls/events/up`, request);
    return await response.json();
}

const scrollDown = async (units) => {
    const request = {
        method: 'POST',
        body: JSON.stringify({units}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/scrolls/events/down`, request);
    return await response.json();
}

const move = async (x, y) => {
    const request = {
        method: 'POST',
        body: JSON.stringify({x, y}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/events/move`, request);
    return await response.json();
}


const api = {
    keyboard: {
        inputText
    },
    mouse: {
        leftClick,
        rightClick,
        scrollUp,
        scrollDown,
        move
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

const toogleLongPressClick = async () => {
    document.querySelectorAll('[data-long-pressable="true"]')
        .forEach((element) => {
            if (!isLongPressed) {
                element.classList.add('active');
            } else {
                element.classList.remove('active');
            }
        });

    isLongPressed = !isLongPressed;
}

const deactivateLongPress = async () =>{
    if(isLongPressed){
        await toogleLongPressClick();
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
        await deactivateLongPress();
    }
}

const bindKeys = () => {

    document.querySelectorAll('[data-long-pressable="true"]')
        .forEach((element) =>{

            element.addEventListener(touchDownEvent, async(event)=>{
                pressTimer = setTimeout(async () => {
                    await toogleLongPressClick();
                }, timeLimitLongPress)
            });

            element.addEventListener(touchReleaseEvent, async(event) => {
                clearTimeout(pressTimer);
                await getKeyAndSendRequest(event, Event.RELEASED);
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
                await getKeyAndSendRequest(ev, Event.RELEASED);
            });
        })
}

const mouseLeftClick = async () => {
    try {
        await api.mouse.leftClick();
    } catch (error) {
        console.error(error);
    }
}

const mouseRightClick = async () => {
    try {
        await api.mouse.rightClick();
    } catch (error) {
        console.error(error);
    }
}

const mouseScrollUp = async (units) => {
    try {
        await api.mouse.scrollUp(units);
    } catch (error) {
        console.error(error);
    }
}

const mouseScrollDown = async (units) => {
    try {
        await api.mouse.scrollDown(units);
    } catch (error) {
        console.error(error);
    }
}

const mouseMove = async (x, y) => {
    try {
        await api.mouse.move(x, y);
    } catch (error) {
        console.error(error);
    }
}

const toggleMouse = () => {

    const mouse = document.getElementById('mouse');
    if (isMouseEnabled) {
        mouse.style.display = 'none';
    } else {
        mouse.style.display = 'block';
    }

    isMouseEnabled = !isMouseEnabled;
}

const calcXY = (ev) => {
    if (isTouchDevice) {
        return {
            x: ev.touches[0].clientX,
            y: ev.touches[0].clientY
        }
    }
    return {
        x: ev.clientX,
        y: ev.clientY
    }
}

const bindMouse = () => {

    document.querySelectorAll('[data-mouse="scroll-x-y"]')
        .forEach((el) => {
            el.addEventListener(touchMoveEvent, async (ev) => {
                const {x, y} = calcXY(ev);
                await mouseMove(x, y);
            })
        });

    document.querySelectorAll('[data-mouse="scroll-y"]')
        .forEach((el) => {
            let lastPosition = 0;
            el.addEventListener(touchMoveEvent, async (ev) => {
                const {y} = calcXY(ev);

                const wichDirection = (newPosition) => {
                    if (newPosition > lastPosition) {
                        return "down"
                    }
                    if (newPosition < lastPosition) {
                        return "up"
                    }
                    return "same";
                }

                const direction = wichDirection(y);
                lastPosition = y;

                if (direction === "up") {
                    await mouseScrollUp(1);
                }
                if (direction === "down") {
                    await mouseScrollDown(1);
                }

            });
        });

    document.querySelectorAll('[data-mouse="toggle"]')
        .forEach((el) => {
            el.addEventListener(touchReleaseEvent, async (ev) => {
                toggleMouse();
            });
        });

    document.querySelectorAll('[data-mouse="left-click"]')
        .forEach((el) => {
            el.addEventListener(touchReleaseEvent, async (ev) => {
                await mouseLeftClick();
            });
        });

    document.querySelectorAll('[data-mouse="right-click"]')
        .forEach((el) => {
            el.addEventListener(touchReleaseEvent, async (ev) => {
                await mouseRightClick();
            });
        });
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadKeyReleaseAudio();
    bindMouse();
    bindKeys();
    toggleMouse();
});


