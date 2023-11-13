import './styles/style.css';

const currentUrl = new URL(window.location.href);
const currentHost = currentUrl.hostname;

const endpoint = `http://${currentHost}:8080/api/v1`;

let hasPointerEvents = (('PointerEvent' in window) || (window.navigator && 'msPointerEnabled' in window.navigator));
let isTouch = (('ontouchstart' in window) || (navigator.MaxTouchPoints > 0) || (navigator.msMaxTouchPoints > 0));

let pointerDownEvent = hasPointerEvents ? 'pointerdown' : isTouch ? 'touchstart' : 'mousedown';
let pointerUpEvent = hasPointerEvents ? 'pointerup' : isTouch ? 'touchend' : 'mouseup';
let pointerMoveEvent = hasPointerEvents ? 'pointermove' : isTouch ? 'touchmove' : 'mousemove';

let isMouseEnabled = false;

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
    if(!text) return;
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
        mouse.style.display = 'flex';
    } else {
        mouse.style.display = 'none';
    }

    isMouseEnabled = !isMouseEnabled;
}

const calcXY = (ev) => {
    if (isTouch && ev.touches !== undefined) {
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
            let lastPositionX = 0;
            let lastPositionY = 0;

            el.addEventListener(pointerDownEvent, async (ev) => {
                const {x, y} = calcXY(ev);
                lastPositionX = x;
                lastPositionY = y;
            })

            el.addEventListener(pointerMoveEvent, async (ev) => {
                const {x, y} = calcXY(ev);
                const wichYDirection = (newPositionY) => {
                    return newPositionY - lastPositionY;
                }

                const wichXDirection = (newPositionX) => {
                    return newPositionX - lastPositionX;
                }

                const xDirection = wichXDirection(x);
                const yDirection = wichYDirection(y);

                lastPositionX = x;
                lastPositionY = y;

                if (xDirection != 0 && yDirection != 0) {
                    await mouseMove(xDirection, yDirection);
                }
            })
        });

    document.querySelectorAll('[data-mouse="scroll-y"]')
        .forEach((el) => {
            let lastPosition = 0;
            el.addEventListener(pointerMoveEvent, async (ev) => {
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
            el.addEventListener(pointerUpEvent, async (ev) => {
                toggleMouse();
            });
        });

    document.querySelectorAll('[data-mouse="left-click"]')
        .forEach((el) => {
            el.addEventListener(pointerUpEvent, async (ev) => {
                await mouseLeftClick();
            });
        });

    document.querySelectorAll('[data-mouse="right-click"]')
        .forEach((el) => {
            el.addEventListener(pointerUpEvent, async (ev) => {
                await mouseRightClick();
            });
        });
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadKeyPressAudio();
    bindMouse();
    bindKeys();
    toggleMouse()
});


