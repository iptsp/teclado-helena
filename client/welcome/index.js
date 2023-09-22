export function WelcomeController(moduleHandler) {

    const init = () => {
        const button = document.getElementById('go-to-default');
        button.addEventListener('click', async () => {
            await moduleHandler.switchTo('default_instance')
        });
    }

    const destroy = () => {

    }

    return {
        init,
        destroy
    }
}
