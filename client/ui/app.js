const endpoint = 'http://localhost:8080/api/v1';

let isShift = false;

const inputText = async (text) => {
    const request = {
        method: 'POST',
        body: JSON.stringify({text}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/keyboards/inputs`, request);
    const json = await response.json();
}

const api = {
    keyboard: {
        inputText
    }
}

const toggleShift = () => {
    const shift = document.querySelector('[data-type="shift"]');

    shift.addEventListener('mousedown', (event) => {
        event.preventDefault();
        event.stopPropagation();
    });

    shift.addEventListener('mouseup', async (event) => {
        document.querySelectorAll('[data-type="key"]')
            .forEach((element) => {
                const textForShift = () => {
                    if (isShift) {
                        return element.dataset.key
                            .toLowerCase();
                    }
                    return element.dataset.key
                        .toUpperCase();
                };
                const newText = textForShift();
                element.dataset.key = newText;
                element.innerHTML = newText;

            });
        isShift = !isShift;
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
                const key = event.target.dataset
                    .key;
                await api.keyboard
                    .inputText(key);
            });
        })
}

document.addEventListener('DOMContentLoaded', () => {
    bindKeys();
    toggleShift();
});


