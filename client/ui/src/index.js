import './styles/style.css';

const keyReleaseAudio = new Audio('./audio/key-release.wav');

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let isShiftActive = false;
let isLongPressed = false;

const timeLimitLongPress = 500;

var pressTimer;

const inputText = async (text) => {
    const request = {
        method: 'POST',
        body: JSON.stringify({text}),
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
    await keyReleaseAudio.play();
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


const simplePress = async (event) =>{
    const {target} = event;
    const calcText = () => {
        if (isLongPressed){
            return findForDatasetValueIncludingAncestors(target,'longPress')
        }
        if (isShiftActive) {
            return findForDatasetValueIncludingAncestors(target, 'keyShift');
        }
        return findForDatasetValueIncludingAncestors(target, 'key');
    }

    try {
        const text = calcText();
        const res = await api.keyboard.inputText(text);
    } catch (error) {
        console.error(error);
    } finally {
        await deactiveShift();
        await deactivateLongPress();
    }
}

const findForDatasetValueIncludingAncestors = (el, attr) => {

    if(el.target && el.target.dataset[attr]) {
        return el.target.dataset[attr];
    }

    if(el.parentElement && el.parentElement.dataset[attr]) {
        return el.parentElement.dataset[attr];
    }

    if(el.parentElement.parentElement && el.parentElement.parentElement.dataset[attr]) {
        return el.parentElement.parentElement.dataset[attr];
    }

    throw new Error(`No dataset value found for element ${el} with attr ${attr}`);
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

            element.addEventListener('mouseup', async (event) => {
                await keyReleaseAllFeedback();
            });
        });

    document.querySelectorAll('[data-key]')
        .forEach((element) => {

            element.addEventListener('mousedown', (event) => {
                event.preventDefault();
                event.stopPropagation();
            });

            element.addEventListener('mouseup', async (event) => {
                await simplePress(event);
            });
        })
}

document.addEventListener('DOMContentLoaded', () => {
    bindKeys();
    bindModals();
});


