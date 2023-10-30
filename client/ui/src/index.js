import './styles/style.css';

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let hasPointerEvents = (('PointerEvent' in window) || (window.navigator && 'msPointerEnabled' in window.navigator));
let isTouch = (('ontouchstart' in window) || (navigator.MaxTouchPoints > 0) || (navigator.msMaxTouchPoints > 0));

let pointerDownEvent = hasPointerEvents ? 'pointerdown' : isTouch ? 'touchstart' : 'mousedown';
let pointerUpEvent = hasPointerEvents ? 'pointerup' : isTouch ? 'touchend' : 'mouseup';

const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
let audioBuffer = null;

const timeLimitLongPress = 500;

const loadKeyPressAudio = async () => {
    try {
        const response = await fetch("./audio/key-press.mp3");
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

const playKeyPressAudio = async () => {
    try {
        const keyPressAudio = await audioCtx.createBufferSource();
        keyPressAudio.buffer = audioBuffer;
        keyPressAudio.connect(audioCtx.destination);
        keyPressAudio.start(0);
    } catch (error) {
        console.error(error);
    }
}

const vibrateOnKeyPress = async () => {
    if (!navigator.vibrate) {
        return;
    }
    return navigator.vibrate(100);
}

const keyPressAllFeedback = async () => {
    await playKeyPressAudio();
    await vibrateOnKeyPress();
}

const setStylePressed = async (element, isPressed) => {
    if (isPressed) {
        element.classList.add('active');
    } else {
        element.classList.remove('active');
    }
};

const setStyleShifted = async (element, isShifted) => {
    if (element.hasAttribute('data-shiftable')) {
        if (isShifted) {
            element.firstElementChild.classList.add('shifted');
        } else {
            element.firstElementChild.classList.remove('shifted');
        }
    }
};

const setStyleLongPressed = async (element, isLongPressed) => {
    if (element.hasAttribute('data-long-press')) {
        if (isLongPressed) {
            element.firstElementChild.classList.add('active');
        } else {
            element.firstElementChild.classList.remove('active');
        }
    }
};

const getKeyAndSendRequest = async (element, isLongPress, eventType) => {
    const text = element.dataset[isLongPress ? 'longPress' : 'key'];
    try {
        await api.keyboard.inputText(text, eventType);
    } catch (error) {
        console.error(error);
    }
}

const bindKeys = () => {
    document.querySelectorAll('button.key')
        .forEach((element) => {
            let pressTimer;

            element.addEventListener(pointerDownEvent, async (event) => {
                element.setPointerCapture(event.pointerId);
                setStylePressed(element, true);
                getKeyAndSendRequest(element, false, Event.PRESSED);

                if (element.hasAttribute('data-long-press')) {
                    pressTimer = setTimeout(async () => {
                        setStyleLongPressed(element, true);
                        getKeyAndSendRequest(element, true, Event.PRESSED);
                    }, timeLimitLongPress);
                }

                if (element.hasAttribute('data-feedback')) {
                    keyPressAllFeedback();
                }
            });

            element.addEventListener(pointerUpEvent, async (event) => {
                setStylePressed(element, false);
                getKeyAndSendRequest(element, false, Event.RELEASED);

                if (element.getAttribute('data-long-press')) {
                    setStyleLongPressed(element, false);
                    clearTimeout(pressTimer);
                }
            });
        });

    document.querySelectorAll('[data-key="shift"]')
        .forEach((element) => {
            element.addEventListener(pointerDownEvent, async (event) => {
                document.querySelectorAll('[data-shiftable]')
                    .forEach((element) => setStyleShifted(element, true));
            });

            element.addEventListener(pointerUpEvent, async (event) => {
                document.querySelectorAll('[data-shiftable]')
                    .forEach((element) => setStyleShifted(element, false));
            });
        });

    // Suppress double-tap magnifying glass on Safari
    document.querySelector('#root').addEventListener('touchend', (event) => {
        event.preventDefault();
    });
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadKeyPressAudio();
    bindKeys();
});


