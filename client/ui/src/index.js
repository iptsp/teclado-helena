//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
// This file is part of Teclado Helena.
//
//     Teclado Helena is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
//     Teclado Helena is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
//     You should have received a copy of the GNU General Public License
// along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

import './styles/style.css';

/** Representa a URL atual da página. */
const currentUrl = new URL(window.location.href);
/** Representa o Host atual. */
const currentHost = currentUrl.hostname;
/** Endereço da API do servidor. */
const endpoint = `http://${currentHost}:8080/api/v1`;
/** Verificação se há eventos do mouse. */
let hasPointerEvents = (('PointerEvent' in window) || (window.navigator && 'msPointerEnabled' in window.navigator));
/** Verificação se há comandos sensíveis ao toque. */
let isTouch = (('ontouchstart' in window) || (navigator.MaxTouchPoints > 0) || (navigator.msMaxTouchPoints > 0));
/** Evento de mouse ou toque pressionado. */
let pointerDownEvent = hasPointerEvents ? 'pointerdown' : isTouch ? 'touchstart' : 'mousedown';
/** Evento de mouse ou toque liberado. */
let pointerUpEvent = hasPointerEvents ? 'pointerup' : isTouch ? 'touchend' : 'mouseup';
/** Evento de movimento do mouse ou toque. */
let pointerMoveEvent = hasPointerEvents ? 'pointermove' : isTouch ? 'touchmove' : 'mousemove';
/** Condicional do modal do mouse aberta ou não. */
let isMouseEnabled = false;

/** Contexto de áudio para reprodução. */
const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
let audioBuffer = null;

/** Tempo limite de toque longo em milissegundos. */
const timeLimitLongPress = 500;

/** Sensibilidade da rolagem de tela. */
const scrollSensitivity = 4;

/** Método que faz o carregamento prévio o áudio de clique. */
const loadKeyPressAudio = async () => {
    try {
        const response = await fetch("./audio/key-press.mp3");
        const buffer = await response.arrayBuffer();
        audioBuffer = await audioCtx.decodeAudioData(buffer);
    } catch (error) {
        console.error(error);
    }
}

/** Teclas sem reprodução de sons falados. */
const MutedKeys = {
    DELETE: 'DELETE',
    BACKSPACE: 'BACKSPACE',
    MOUSE: 'MOUSE'
};

/** Eventos de mouse/toque. */
const Event = {
    PRESSED: "PRESSED",
    RELEASED: "RELEASED"
}

/**
 * Envio da tecla ativada para o servidor
 * @param {string} text - Tecla a ser enviada
 * @param {event} event - Tipo de evento
 * @return {json} Resposta de execução
 */
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

/**
 * Envio do comando completo do botão esquerdo do mouse
 * @return {json} Resposta de execução
 */
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

/**
 * Envio do comando pressionar do esquerdo direito do mouse
 * @return {json} Resposta de execução
 */
const leftPress = async () => {
    const request = {
        method: 'POST',
        body: JSON.stringify({}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/buttons/left/events/press`, request);
    return await response.json();
}

/**
 * Envio do comando liberar do botão esquerdo do mouse
 * @return {json} Resposta de execução
 */
const leftRelease = async () => {
    const request = {
        method: 'POST',
        body: JSON.stringify({}),
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const response = await fetch(`${endpoint}/systems/mouses/buttons/left/events/release`, request);
    return await response.json();
}

/**
 * Envio do comando completo do botão direito do mouse
 * @return {json} Resposta de execução
 */
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

/**
 * Envio do comando rolar página para cima
 */
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

/**
 * Envio do comando rolar página para baixo
 */
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

/**
 * Envio do comando de movimento do mouse
 */
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
        leftPress,
        leftRelease,
        rightClick,
        scrollUp,
        scrollDown,
        move
    }
}

/**
 * Reproduz áudio de tecla com toque longo
 * @param {element} element - Tecla que será reproduzido o áudio.
 */
const playLongPressAudio = async (element) => {
    try {
        var audioName = 'key-press';
        if (element != undefined && element.dataset != undefined && element.dataset['longPress'] != undefined) {
            var audioName = element.dataset['longPress'].toUpperCase();
        }
        playAudio(audioName);
    } catch (error) {
        console.error(error);
    }
}

/**
 * Verifica se a tecla deve reproduzir áudio.
 * @param {string} keyName - Nome da tecla.
 */
const isMuted = (keyName) => {
     return Object.values(MutedKeys).indexOf(keyName) > -1;
}

/**
 * Reproduzir áudio da tecla acionada.
 * @param {element} element - Tecla para reprodução do áudio.
 */
const playKeyPressAudio = async (element) => {
    try {
        var audioName = 'key-press';
        if (element != undefined && element.dataset != undefined) {
            if (element.dataset['key'] != undefined) {
                var audioName = element.dataset['key'].toUpperCase();
            }
            if (element.dataset['shiftable'] != undefined
            && element.dataset['shiftable'] != "") {
                var shiftActive = document.querySelector('[data-key=shift]').classList.contains("activated");
                if (shiftActive) {
                    audioName = element.dataset['shiftable'].toUpperCase();
                }
            }
        }
        playAudio(audioName);
    } catch (error) {
        console.error(error);
    }
}

/**
 * Reproduzir áudio.
 * @param {string} name - Nome do áudio a ser reproduzido
 */
const playAudio = async (name) => {
    try {
        if (!name || isMuted(name)) name = 'key-press';
        const response = await fetch(`./audio/${name}.mp3`);
        const buffer = await response.arrayBuffer();
        const audioBuffer = await audioCtx.decodeAudioData(buffer);

        const source = audioCtx.createBufferSource();
        source.buffer = audioBuffer;
        source.connect(audioCtx.destination);
        source.start();
    } catch (error) {
        console.error(error);
    }
}

/**
 * Reproduzir a vibração do dispositivo, se houver.
 */
const vibrateOnKeyPress = async () => {
    if (!navigator.vibrate) {
        return;
    }
    return navigator.vibrate(100);
}

/**
 * Reproduzir os feedbacks da tecla.
 * @param {element} element - Tecla a ter feedback acionado
 */
const keyPressAllFeedback = async (element) => {
    await playKeyPressAudio(element);
    await vibrateOnKeyPress();
}

/**
 * Altera a aparência da tecla acionada.
 * @param {string} name - Nome do áudio a ser reproduzido
 */
const setStylePressed = async (element, isPressed) => {
    if (element.classList.contains('activated')) return;
    if (isPressed) {
        element.classList.remove('active-fade');
        element.classList.add('active');
    } else {
        element.classList.remove('active');
        element.classList.add('active-fade');
    }
};

/**
 * Altera a aparência da tecla quando houver o atributo 'data-shiftable'.
 * @param {element} element - Tecla que terá sua aparência alterada.
 * @param {boolean} isShifted - Váriavel verdadeira se shift acionado.
 */
const setStyleShifted = async (element, isShifted) => {
    if (element.hasAttribute('data-shiftable')) {
        if (isShifted) {
            element.firstElementChild.classList.add('shifted');
        } else {
            element.firstElementChild.classList.remove('shifted');
        }
    }
};

/**
 * Altera a aparência da tecla quando houver o atributo 'data-long-press'.
 * @param {element} element - Tecla que terá sua aparência alterada.
 * @param {boolean} isLongPressed - Váriavel verdadeira se toque longo foi acionado.
 */
const setStyleLongPressed = async (element, isLongPressed) => {
    if (element.hasAttribute('data-long-press')) {
        if (isLongPressed) {
            element.firstElementChild.classList.add('active');
        } else {
            element.firstElementChild.classList.remove('active');
        }
    }
};

/**
 * Aciona a tela cheia.
 */
const setFullScreen = async () => {
    if(document.fullscreenElement == null) {
        document.documentElement.requestFullscreen();
    }
};

/**
 * Altera a aparência da tecla quando houver o atributo 'data-long-press'.
 * @param {element} element - Tecla que terá sua aparência alterada.
 * @param {boolean} isLongPress - Váriavel verdadeira se toque longo foi acionado.
 * @param {event} isLongPress - Váriavel verdadeira se toque longo foi acionado.
 */
const getKeyAndSendRequest = async (element, isLongPress, eventType) => {
    const text = element.dataset[isLongPress ? 'longPress' : 'key'];
    if(!text) return;
    try {
        await api.keyboard.inputText(text, eventType);
    } catch (error) {
        console.error(error);
    }
}

/**
 * Desativar as teclas de aderência.
 * @param {element} element - Tecla que terá sua aparência alterada.
 * @param {boolean} isLongPress - Váriavel verdadeira se toque longo foi acionado.
 * @param {event} isLongPress - Váriavel verdadeira se toque longo foi acionado.
 */
const deactivateStickyKeys = async (el) => {
    if (el.dataset['key'] != 'shift' &&
        el.dataset['key'] != 'ctrl' &&
        el.dataset['key'] != 'alt')
    document.querySelectorAll('[data-key=shift], [data-key=ctrl], [data-key=alt]')
        .forEach((element) => {
            if (el != element && element.classList.contains('activated')) {
                getKeyAndSendRequest(element, false, Event.RELEASED);
                element.classList.remove('activated');
            }
        });
    shiftingKeys(false);
}

/**
 * Altera o estilo das teclas com "data-shiftable".
 * @param {boolean} active - Identifica se deve ser ativado ou desativado.
 */
const shiftingKeys = async (active) => {
    document.querySelectorAll('[data-shiftable]')
        .forEach((element) => {
            setStyleShifted(element, active);
        });
}

/** Atribuição de eventos nas teclas. */
const bindKeys = () => {
    document.querySelectorAll('button.key')
        .forEach((element) => {
            let pressTimer;

            /** Atribuição do evento de clique ao pressionar a tecla. */
            element.addEventListener(pointerDownEvent, async (event) => {
                element.setPointerCapture(event.pointerId);
                setStylePressed(element, true);
                /** Case seja uma das teclas de aderência, não é enviado o comando para o servidor quando ativo. */
                if ((element.dataset['key'] != 'shift' &&
                    element.dataset['key'] != 'ctrl' &&
                    element.dataset['key'] != 'alt') ||
                    (element.dataset['key'] == 'shift' && !element.classList.contains('activated')) ||
                    (element.dataset['key'] == 'ctrl' && !element.classList.contains('activated')) ||
                    (element.dataset['key'] == 'alt' && !element.classList.contains('activated'))
                ) {
                    getKeyAndSendRequest(element, false, Event.PRESSED);
                }
                /** Inicia a contagem de tempo ao pressionar a tecla caso tenha a opção de pressionamento longo,
                 * altera o estilo da tecla e envia o comando quando a contagem de tempo se zerar.
                 */
                if (element.hasAttribute('data-long-press')) {
                    pressTimer = setTimeout(async () => {
                        setStyleLongPressed(element, true);
                        getKeyAndSendRequest(element, true, Event.PRESSED);
                        playLongPressAudio(element);
                    }, timeLimitLongPress);
                }

                /** Executa os eventos de feedback da tecla. */
                if (element.hasAttribute('data-feedback')) {
                    keyPressAllFeedback(element);
                }
            });

            /** Atribuição do evento de clique ao liberar a tecla. */
            element.addEventListener(pointerUpEvent, async (event) => {
                setFullScreen();
                setStylePressed(element, false);
                /**
                 * Caso não seja uma das teclas de aderência,
                 * todas as teclas de aderência são desativadas
                 * e o comando é enviado ao servidor.
                 */
                if ((element.dataset['key'] != 'shift' &&
                    element.dataset['key'] != 'ctrl' &&
                    element.dataset['key'] != 'alt') ||
                    (element.dataset['key'] == 'shift' && element.classList.contains('activated')) ||
                    (element.dataset['key'] == 'ctrl' && element.classList.contains('activated')) ||
                    (element.dataset['key'] == 'alt' && element.classList.contains('activated'))
                    ) {
                    getKeyAndSendRequest(element, false, Event.RELEASED);
                    deactivateStickyKeys(element);
                }

                /** Encerra a contagem de tempo e altera o estilo para o estado inicial. */
                if (element.getAttribute('data-long-press')) {
                    setStyleLongPressed(element, false);
                    clearTimeout(pressTimer);
                }
            });
        });

    /** Ativa a tecla shift ao pressionar e altera o estilo de teclas referentes ao shift. */
    const shiftingKey = async () => {
        document.querySelectorAll('[data-key="shift"]')
            .forEach((element) => {
                element.addEventListener(pointerUpEvent, async (event) => {
                    if (element.classList.contains('activated')) {
                        element.classList.remove('activated');
                        shiftingKeys(false);
                    } else {
                        element.classList.add('activated');
                        element.classList.remove('active-fade');
                        shiftingKeys(true);
                    }
                });
            });
        }

        /** Inicia o estilo das teclas para o padrão. */
        shiftingKey();

        /** Ativa a tecla ctrl e alt ao pressionar. */
        document.querySelectorAll('[data-key="ctrl"], [data-key="alt"]')
                .forEach((element) => {
                    element.addEventListener(pointerUpEvent, async (event) => {
                        if (element.classList.contains('activated')) {
                            element.classList.remove('activated');
                        } else {
                            element.classList.add('activated');
                            element.classList.remove('active-fade');
                        }
                    });
                });

    /** Impede o evento de aumentar no navegador Safari */
    document.querySelector('#root').addEventListener('touchend', (event) => {
        event.preventDefault();
    });
}

/** Envia o comando de clique do botão esquerdo do mouse para o servidor */
const mouseLeftClick = async () => {
    try {
        await api.mouse.leftClick();
    } catch (error) {
        console.error(error);
    }
}

/** Envia o comando de clique ao pressionar o botão esquerdo do mouse para o servidor */
const mouseLeftPress = async () => {
    try {
        await api.mouse.leftPress();
    } catch (error) {
        console.error(error);
    }
}

/** Envia o comando de clique ao liberar o botão esquerdo do mouse para o servidor */
const mouseLeftRelease = async () => {
    try {
        await api.mouse.leftRelease();
    } catch (error) {
        console.error(error);
    }
}

/** Envia o comando de clique do botão direito do mouse para o servidor */
const mouseRightClick = async () => {
    try {
        await api.mouse.rightClick();
    } catch (error) {
        console.error(error);
    }
}

/** Envia o comando de rolagem de página para cima do mouse para o servidor */
const mouseScrollUp = async (units) => {
    try {
        await api.mouse.scrollUp(units);
    } catch (error) {
        console.error(error);
    }
}

/** Envia o comando de rolagem de página para baixo do mouse para o servidor */
const mouseScrollDown = async (units) => {
    try {
        await api.mouse.scrollDown(units);
    } catch (error) {
        console.error(error);
    }
}

/** Envia o comando de movimento do ponteiro do mouse para o servidor */
const mouseMove = async (x, y) => {
    try {
        await api.mouse.move(x, y);
    } catch (error) {
        console.error(error);
    }
}

/** Ativa a tela de comandos do mouse. */
const toggleMouse = () => {

    const mouse = document.getElementById('mouse');
    if (isMouseEnabled) {
        mouse.style.display = 'flex';
    } else {
        mouse.style.display = 'none';
    }

    isMouseEnabled = !isMouseEnabled;
}

/**
 * Coleta os dados de X e Y ao clique na área de touchpad do mouse.
 * @param {event} ev - Evento
 * @return {object} - Objeto contendo X e Y do evento de clique.
 */
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

/** Atribuição dos eventos do mouse. */
const bindMouse = () => {
    document.querySelectorAll('[data-mouse="scroll-x-y"]')
        .forEach((el) => {
            let lastPositionX = 0;
            let lastPositionY = 0;

            /** Evento de clique ao pressionar área de controle do mouse. */
            el.addEventListener(pointerDownEvent, async (ev) => {
                const {x, y} = calcXY(ev);
                lastPositionX = x;
                lastPositionY = y;
            })

            /** Evento de reposisionamento do ponteiro do mouse na tela
             * ao movimentar na área de controle do mouse.
             */
            el.addEventListener(pointerMoveEvent, async (ev) => {
                const {x, y} = calcXY(ev);
                const whichYDirection = (newPositionY) => {
                    return newPositionY - lastPositionY;
                }

                const whichXDirection = (newPositionX) => {
                    return newPositionX - lastPositionX;
                }

                let xDirection = whichXDirection(x);
                let yDirection = whichYDirection(y);

                if (Math.abs(xDirection) > 10) xDirection *= 2;
                if (Math.abs(yDirection) > 10) yDirection *= 2;

                lastPositionX = x;
                lastPositionY = y;

                if (xDirection != 0 && yDirection != 0) {
                    await mouseMove(xDirection, yDirection);
                }
            })
        });

    /** Evento da tela de rolagem de página. */
    document.querySelectorAll('[data-mouse="scroll-y"]')
        .forEach((el) => {
            let lastPosition = 0;
            let scrollCounterUp = 0;
            let scrollCounterDown = 0;
            /** Evento de movimento ao movimentar na tela de rolagem de página. */
            el.addEventListener(pointerMoveEvent, async (ev) => {
                const {y} = calcXY(ev);

                const whichDirection = (newPosition) => {
                    if (newPosition > lastPosition) {
                        return "down"
                    }
                    if (newPosition < lastPosition) {
                        return "up"
                    }
                    return "same";
                }

                const direction = whichDirection(y);
                lastPosition = y;

                if (direction === "up") {
                    scrollCounterDown = 0;
                    scrollCounterUp += 1;
                    if (scrollCounterUp > scrollSensitivity){
                        await mouseScrollUp(1);
                        scrollCounterUp = 0;
                    }
                }
                if (direction === "down") {
                    scrollCounterUp = 0;
                    scrollCounterDown += 1;
                    if (scrollCounterDown > scrollSensitivity){
                        await mouseScrollDown(1);
                        scrollCounterDown = 0;
                    }
                }

            });
        });

    /** Atribui o evento de clique. */
    document.querySelectorAll('[data-mouse="toggle"]')
        .forEach((el) => {
            el.addEventListener(pointerUpEvent, async (ev) => {
                toggleMouse();
            });
        });

    /** Atribui o evento de clique ao pressionar o botão esquerdo do mouse. */
    document.querySelectorAll('[data-mouse="left-click"]')
        .forEach((el) => {
            el.addEventListener(pointerDownEvent, async (ev) => {
                await mouseLeftPress();
                var element = ev.target;
                if (element.hasAttribute('data-feedback')) {
                    keyPressAllFeedback();
                }
            });
            el.addEventListener(pointerUpEvent, async (ev) => {
                await mouseLeftRelease();
            });
        });

    /** Atribui o evento de clique ao pressionar o botão direito do mouse. */
    document.querySelectorAll('[data-mouse="right-click"]')
        .forEach((el) => {
            el.addEventListener(pointerUpEvent, async (ev) => {
                await mouseRightClick();
                var element = ev.target;
                if (element.hasAttribute('data-feedback')) {
                    keyPressAllFeedback();
                }
            });
        });
}

/** Executa as funções ao iniciar a página. */
document.addEventListener('DOMContentLoaded', async () => {
    await loadKeyPressAudio();
    bindMouse();
    bindKeys();
    toggleMouse()
});


