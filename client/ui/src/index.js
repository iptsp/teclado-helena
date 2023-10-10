import './styles/style.css';

const keyReleaseAudio = new Audio('./audio/key-release.wav');

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let isShiftActive = false;
let isCtrlActive = false;

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

const disableCtrl = async () => {
    if (isCtrlActive) {
        isCtrlActive = false;
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

const toogleCtrl = async () => {

    document.querySelectorAll('[data-ctrl="true"]')
        .forEach((element) => {
            if (!isCtrlActive) {
                console.log('active');
                element.classList.add('active');
            } else {
                console.log('deactive');
                element.classList.remove('active');
            }
        });

    isCtrlActive = !isCtrlActive;
}

const deactiveShift = async () => {
    if (isShiftActive) {
        await toogleShift();
    }
}

const deactiveCtrl = async () => {
    if (isCtrlActive) {
        await toogleCtrl();
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

    document.querySelectorAll('[data-ctrl="true"]')
            .forEach((element) => {
                element.addEventListener('mouseup', async (event) => {
                    await keyReleaseAllFeedback();
                    await toogleCtrl();
                });
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

                const {target} = event;
                const calcText = () => {
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
                }
            });
        })
}

document.addEventListener('DOMContentLoaded', () => {
    bindKeys();
    bindModals();
});


