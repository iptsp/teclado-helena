import './styles/style.css';

const keyReleaseAudio = new Audio('./audio/key-release.wav');

const endpoint = 'http://192.168.3.108:8080/api/v1';
let isCaps = false;

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

const keyReleaseFeedback = async () => {
    await playKeyReleaseAudio();
    await vibrateOnKeyRelease();
}

const toggleCaps = () => {
    const caps = document.querySelector('[data-type="caps"]');

    caps.addEventListener('mousedown', (event) => {
        event.preventDefault();
        event.stopPropagation();
    });

    caps.addEventListener('mouseup', async (event) => {

        await keyReleaseFeedback();

        document.querySelectorAll('[data-type="key"]')
            .forEach((element) => {
                const textForCaps = () => {
                    if (isCaps) {
                        return element.dataset.key
                            .toLowerCase();
                    }
                    return element.dataset.key
                        .toUpperCase();
                };
                const newText = textForCaps();
                element.dataset.key = newText;
                element.innerHTML = newText;

            });
        isCaps = !isCaps;
    });
}


const bindKeys = () => {

    document.querySelectorAll('[data-type="key"]')
        .forEach((element) => {

            element.addEventListener('mousedown', (event) => {
                event.preventDefault();
                event.stopPropagation();
            });

            element.addEventListener('mouseup', async (event) => {

                await keyReleaseFeedback();

                const key = event.target.dataset
                    .key;

                try {
                    const text = await api.keyboard.inputText(key);
                } catch (error) {
                    console.error(error);
                }
            });
        })
}

document.addEventListener('DOMContentLoaded', () => {
    bindKeys();
    toggleCaps();
});


