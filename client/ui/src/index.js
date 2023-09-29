import './styles/style.css';

const keyReleaseAudio = new Audio('./audio/key-release.wav');

const endpoint = 'http://192.168.3.108:8080/api/v1';

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
                body.style.top = `${rect.top - 90}px`;
                body.style.left = `${rect.left + 5}px`;

                const pin = element.querySelector('.pin');
                pin.addEventListener('click', (event) => {
                   element.classList.remove('show');
                });
            });
        });
}

const bindKeys = () => {

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

                const { target } = event;
                const { parentElement } = target;
                const { parentElement: parentParentElement } = parentElement;

                const key = target.dataset
                    .key || parentElement.dataset.key
                    || parentParentElement.dataset.key;


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
    bindModals();
});


