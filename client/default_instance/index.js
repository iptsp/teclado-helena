export function DefaultInstanceController(moduleHandler) {

    const bindEvents = () => {
        const button = document.getElementById('go-to-welcome');
        button.addEventListener('click', async () => {
            await moduleHandler.switchTo('welcome')
        });
    }

    const init = () => {
        bindEvents();
    }

    const destroy = () => {

    }

    return {
        init,
        destroy
    }
}
