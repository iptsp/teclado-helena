import './styles/style.css';

const keyReleaseAudio = new Audio('./audio/key-release.wav');

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let isTouchDevice = 'ontouchstart' in window || navigator.maxTouchPoints;
let touchDownEvent = isTouchDevice ? 'touchstart' : 'mousedown';
let touchReleaseEvent = isTouchDevice ? 'touchend' : 'mouseup';

let isShiftActive = false;
let isLongPressed = false;

const timeLimitLongPress = 500;

var pressTimer;

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
        await keyReleaseAudio.play();
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

const bindModals = () => {
    document.querySelectorAll('[data-modal]')
        .forEach((element) => {
            const attachTo = element.dataset
                .attachTo;
            const attacher = document.querySelector(attachTo);
            attacher.addEventListener('click', (event) => {
                element.classList.add('show');

                const body = element.querySelector('.body');
                const rect = attacher.getBoundingClientRect();
                body.style.top = `${rect.top - 70}px`;
                body.style.left = `${rect.left + 5}px`;

                const pin = element.querySelector('.pin');
                pin.addEventListener('click', (event) => {
                    element.classList.remove('show');
                });
            });
        });
}

const disableShift = async () => {
    if (isShiftActive) {
        isShiftActive = false;
    }
}

const toogleShift = async () => {

    document.querySelectorAll('[data-shiftable="true"]')
        .forEach((element) => {
            if (!isShiftActive) {
                element.classList.add('active');
            } else {
                element.classList.remove('active');
            }
        });

    document.querySelectorAll('[data-shifter="true"]')
        .forEach((element) => {
            if (!isShiftActive) {
                element.classList.add('active');
            } else {
                element.classList.remove('active');
            }
        });

    document.querySelectorAll('[data-key] .text')
        .forEach((element) => {
            if (isShiftActive) {
                element.textContent = findForDatasetValueIncludingAncestors(element, 'key');
            } else {
                element.textContent = findForDatasetValueIncludingAncestors(element, 'keyShift');
            }
        });

    isShiftActive = !isShiftActive;
}

const deactiveShift = async () => {
    if (isShiftActive) {
        await toogleShift();
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
    const {target} = ev;

    try {
        const text = findForDatasetValueIncludingAncestors(target, 'key');
        await api.keyboard.inputText(text, eventType);
    } catch (error) {
        console.error(error);
    } finally {
        await keyReleaseAllFeedback();
    }
}

const bindKeys = () => {

    document.querySelectorAll('[data-shifter="true"]')
        .forEach((element) => {

            element.addEventListener('mouseup', async (event) => {
                await keyReleaseAllFeedback();
                await toogleShift();
            });
        });

    document.querySelectorAll('[data-long-pressable="true"]')
        .forEach((element) =>{

            element.addEventListener('mousedown', async(event)=>{
                pressTimer = setTimeout(async ()=>{
                    await toogleLongPressClick();
                }, timeLimitLongPress)
            });

            element.addEventListener('mouseup', async(event) => {
                clearTimeout(pressTimer)
                await simplePress(event);
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

document.addEventListener('DOMContentLoaded', () => {
    bindKeys();
});


