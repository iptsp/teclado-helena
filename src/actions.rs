use enigo::*;
use serde_derive::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
#[serde(tag = "action", content = "data")]
pub enum InputAction {
    MouseMove {
        dx: i32,
        dy: i32,
    },
    MouseClick {
        button: MouseButton,
        state: ButtonState,
    },
    MouseScroll {
        direction: ScrollDirection,
        amount: i32,
    },
    KeyPress {
        key: String,
        state: ButtonState,
    },
}

// Supporting types for our actions
#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "lowercase")]
pub enum MouseButton {
    Left,
    Right,
}

#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "lowercase")]
pub enum ButtonState {
    Press,
    Release,
}

#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "lowercase")]
pub enum ScrollDirection {
    Up,
    Down,
}

// Helper function to deserialize JSON to an InputAction
pub fn deserialize_action(json: &str) -> Result<InputAction, serde_json::Error> {
    serde_json::from_str(json)
}

pub fn process_mouse_move(dx: i32, dy: i32) {
    println!("Move mouse by ({}, {})", dx, dy);
    let mut enigo = Enigo::new();
    enigo.mouse_move_relative(dx, dy);
}

pub fn process_mouse_click(button: MouseButton, state: ButtonState) {
    match (button, state) {
        (MouseButton::Left, ButtonState::Press) => {
            println!("Press left button");
            let mut enigo = Enigo::new();
            enigo.mouse_down(enigo::MouseButton::Left);
        }
        (MouseButton::Left, ButtonState::Release) => {
            println!("Release left button");
            let mut enigo = Enigo::new();
            enigo.mouse_up(enigo::MouseButton::Left);
        }
        (MouseButton::Right, ButtonState::Press) => println!("Press right button"),
        (MouseButton::Right, ButtonState::Release) => println!("Release right button"),
    }
}

pub fn process_scroll(direction: ScrollDirection, amount: i32) {
    match direction {
        ScrollDirection::Up => {
            println!("Scroll up by {}", amount);
            let mut enigo = Enigo::new();
            enigo.mouse_scroll_y(-amount);
        }
        ScrollDirection::Down => {
            println!("Scroll down by {}", amount);
            let mut enigo = Enigo::new();
            enigo.mouse_scroll_y(amount);
        }
    }
}

fn get_key(key: String) -> Result<enigo::keycodes::Key, &'static str> {
    match key.as_str() {
        "esc" => Ok(enigo::keycodes::Key::Escape),
        "1" => Ok(enigo::keycodes::Key::Layout('1')),
        "2" => Ok(enigo::keycodes::Key::Layout('2')),
        "3" => Ok(enigo::keycodes::Key::Layout('3')),
        "4" => Ok(enigo::keycodes::Key::Layout('4')),
        "5" => Ok(enigo::keycodes::Key::Layout('5')),
        "6" => Ok(enigo::keycodes::Key::Layout('6')),
        "7" => Ok(enigo::keycodes::Key::Layout('7')),
        "8" => Ok(enigo::keycodes::Key::Layout('8')),
        "9" => Ok(enigo::keycodes::Key::Layout('9')),
        "0" => Ok(enigo::keycodes::Key::Layout('0')),
        "minus" => Ok(enigo::keycodes::Key::Layout('-')),
        "equals" => Ok(enigo::keycodes::Key::Layout('=')),
        "quote" => Ok(enigo::keycodes::Key::Layout('\'')),
        "slash" => Ok(enigo::keycodes::Key::AbntC1),
        "semicolon" => Ok(enigo::keycodes::Key::Layout(';')),
        "comma" => Ok(enigo::keycodes::Key::Layout(',')),
        "period" => Ok(enigo::keycodes::Key::Layout('.')),
        "tilde" => Ok(enigo::keycodes::Key::Layout('~')),
        "acute" => Ok(enigo::keycodes::Key::Layout('´')),
        "delete" => Ok(enigo::keycodes::Key::Delete),
        "backspace" => Ok(enigo::keycodes::Key::Backspace),
        "q" => Ok(enigo::keycodes::Key::Layout('q')),
        "w" => Ok(enigo::keycodes::Key::Layout('w')),
        "e" => Ok(enigo::keycodes::Key::Layout('e')),
        "r" => Ok(enigo::keycodes::Key::Layout('r')),
        "t" => Ok(enigo::keycodes::Key::Layout('t')),
        "y" => Ok(enigo::keycodes::Key::Layout('y')),
        "u" => Ok(enigo::keycodes::Key::Layout('u')),
        "i" => Ok(enigo::keycodes::Key::Layout('i')),
        "o" => Ok(enigo::keycodes::Key::Layout('o')),
        "p" => Ok(enigo::keycodes::Key::Layout('p')),
        "a" => Ok(enigo::keycodes::Key::Layout('a')),
        "s" => Ok(enigo::keycodes::Key::Layout('s')),
        "d" => Ok(enigo::keycodes::Key::Layout('d')),
        "f" => Ok(enigo::keycodes::Key::Layout('f')),
        "g" => Ok(enigo::keycodes::Key::Layout('g')),
        "h" => Ok(enigo::keycodes::Key::Layout('h')),
        "j" => Ok(enigo::keycodes::Key::Layout('j')),
        "k" => Ok(enigo::keycodes::Key::Layout('k')),
        "l" => Ok(enigo::keycodes::Key::Layout('l')),
        "enter" => Ok(enigo::keycodes::Key::Return),
        "z" => Ok(enigo::keycodes::Key::Layout('z')),
        "x" => Ok(enigo::keycodes::Key::Layout('x')),
        "c" => Ok(enigo::keycodes::Key::Layout('c')),
        "v" => Ok(enigo::keycodes::Key::Layout('v')),
        "b" => Ok(enigo::keycodes::Key::Layout('b')),
        "n" => Ok(enigo::keycodes::Key::Layout('n')),
        "m" => Ok(enigo::keycodes::Key::Layout('m')),
        "up-arrow" => Ok(enigo::keycodes::Key::UpArrow),
        "ctrl" => Ok(enigo::keycodes::Key::Control),
        "alt" => Ok(enigo::keycodes::Key::Alt),
        "space" => Ok(enigo::keycodes::Key::Space),
        "left-arrow" => Ok(enigo::keycodes::Key::LeftArrow),
        "home" => Ok(enigo::keycodes::Key::Home),
        "down-arrow" => Ok(enigo::keycodes::Key::DownArrow),
        "right-arrow" => Ok(enigo::keycodes::Key::RightArrow),
        "end" => Ok(enigo::keycodes::Key::End),
        "cedilla" => Ok(enigo::keycodes::Key::OEM1),
        "shift" => Ok(enigo::keycodes::Key::Raw(0x2A)),
        _ => Err("Unknown key"),
    }
}

pub fn process_key_press(key: String, state: ButtonState) {
    match state {
        ButtonState::Press => {
            println!("Press {:?} key", key);
            let mut enigo = Enigo::new();
            if let Ok(key) = get_key(key) {
                enigo.key_down(key);
            } else {
                println!("Unknown key");
            }
        }
        ButtonState::Release => {
            println!("Release {:?} key", key);
            let mut enigo = Enigo::new();
            if let Ok(key) = get_key(key) {
                enigo.key_up(key);
            } else {
                println!("Unknown key");
            }
        }
    }
}

pub fn process_action(action: InputAction) {
    match action {
        InputAction::MouseMove { dx, dy } => process_mouse_move(dx, dy),
        InputAction::MouseClick { button, state } => process_mouse_click(button, state),
        InputAction::MouseScroll { direction, amount } => process_scroll(direction, amount),
        InputAction::KeyPress { key, state } => process_key_press(key, state),
    }
}
