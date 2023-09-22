import {WelcomeController} from './welcome/index.js';
import {DefaultInstanceController} from './default_instance/index.js';


function ModuleHandler() {

    let modules = [];
    let currentModule = null;

    const register = (id, module) => {
        modules[id] = {
            name: id,
            instance: module
        };
    }

    const switchTo = async (moduleName) => {
        const module = modules[moduleName];

        if (currentModule) {

            const { name, instance } = currentModule;

            //unload css
            const link = document.querySelector(`link[href="${name}/style.css"]`);
            link.remove();

            //unload html
            const root = document.getElementById("root")
            root.innerHTML = '';

            //destroy module
            instance.destroy();
        }

        const { name, instance } = module;

        //load css from module
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = `${name}/style.css`;
        document.head.appendChild(link);

        //load html from module
        let res = await fetch(`${name}/index.html`);
        let html = await res.text();
        const root = document.getElementById("root")
        root.innerHTML = html;

        //init module
        instance.init();

        currentModule = module;

    }

    return {
        switchTo,
        register
    }
}

const moduleHandler = new ModuleHandler();
const defaultInstance = new DefaultInstanceController(moduleHandler);
const welcome = new WelcomeController(moduleHandler);
moduleHandler.register('default_instance', defaultInstance);
moduleHandler.register('welcome', welcome);
moduleHandler.switchTo('welcome')
    .catch(console.error);
