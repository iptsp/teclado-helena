//! # Processamento de ações enviadas pelo cliente
//!
//! Módulo para processar e simular ações do usuário como movimento do mouse, cliques, roda do mouse e teclas do teclado.

use enigo::*;
use serde_derive::{Deserialize, Serialize};

/// Um enum representando todas as ações possíveis que o usuário pode enviar.
/// 
/// Cada ação pode ser serializada e desserializada em JSON.
#[derive(Serialize, Deserialize, Debug)]
#[serde(tag = "action", content = "data")]
pub enum InputAction {
    /// Representa uma ação de movimento do mouse com os deltas especificados
    MouseMove {
        /// A movimentação na direção X
        dx: i32,
        /// A movimentação na direção Y
        dy: i32,
    },
    /// Representa uma ação de clique do mouse
    MouseClick {
        /// O botão que está sendo clicado
        button: MouseButton,
        /// O estado do botão (pressionado ou soltado)
        state: ButtonState,
    },
    /// Representa uma ação de rolagem da roda do mouse
    MouseScroll {
        /// A direção de rolagem
        direction: ScrollDirection,
        /// A quantidade de rolagem
        amount: i32,
    },
    /// Representa uma ação de pressionamento de tecla
    KeyPress {
        /// A tecla representada em uma string
        key: String,
        /// O estado da tecla (pressionada ou soltada)
        state: ButtonState,
    },
}

/// Representa os botões do mouse esquerdo e direito
#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "lowercase")]
pub enum MouseButton {
    Left,
    Right,
}

/// Representa o estado do botão de mouse ou da tecla do teclado
#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "lowercase")]
pub enum ButtonState {
    Press,
    Release,
}

/// Representa as possíveis direções de rolagem da roda do mouse
#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "lowercase")]
pub enum ScrollDirection {
    Up,
    Down,
}

/// Desserializa uma string JSON em um [`InputAction`].
///
/// # Parâmetros
///
/// * `json` - Uma string slice que contém a string JSON a ser desserializada.
///
/// # Retorno
///
/// Esta função retorna um Result com [`InputAction`] caso seja bem-sucedida, ou [`serde_json::Error`] caso a desserialização falhe.
///
/// # Exemplo
///
/// ```
/// let json = r#"{ "action": "MouseMove", "data": { "dx": 10, "dy": 5 } }"#;
/// let action = deserialize_action(json).unwrap();
/// ```
pub fn deserialize_action(json: &str) -> Result<InputAction, serde_json::Error> {
    serde_json::from_str(json)
}

/// Simula um movimento de mouse relativo
///
/// # Parâmetros
///
/// * `dx` - O delta a mover na direção X.
/// * `dy` - O delta a mover na direção Y.
///
/// # Exemplo
///
/// ```
/// process_mouse_move(10, 5);
/// // Isso moverá o mouse por (10, 5)
/// ```
pub fn process_mouse_move(dx: i32, dy: i32) {
    println!("Move mouse by ({}, {})", dx, dy);
    let mut enigo = Enigo::new();
    enigo.mouse_move_relative(dx, dy);
}

/// Simula uma ação de clique
///
/// # Parâmetros
///
/// * `button` - O botão [`MouseButton`] que foi clicado.
/// * `state` - O estado [`ButtonState`] do botão.
///
/// # Exemplo
///
/// ```
/// process_mouse_click(MouseButton::Left, ButtonState::Press);
/// // Isso simulará um pressionamento do botão esquerdo do mouse
/// 
/// process_mouse_click(MouseButton::Left, ButtonState::Release);
/// // Isso simulará um soltamento do botão esquerdo do mouse
/// ```
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

/// Processa uma ação de rolagem da roda do mouse
///
/// # Parâmetros
///
/// * `direction` - A direção [`ScrollDirection`] de rolagem da roda do mouse.
/// * `amount` - A quantidade de scroll a ser aplicada.
///
/// # Exemplo
///
/// ```
/// process_scroll(ScrollDirection::Up, 3);
/// // Isso simulará uma rolagem da roda de mouse para cima de 3 unidades
/// ```
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

/// Converte uma representação em string de uma tecla em um [`enigo::Key`].
///
/// # Parâmetros
///
/// * `key` - Uma string representando a tecla.
///
/// # Retorno
///
/// Esta função retorna a tecla [`enigo::Key`] caso conhecida,
/// ou um erro `&'static str` caso a tecla não seja encontrada.
///
/// # Exemplo
///
/// ```
/// let key = get_key("a".to_string()).unwrap();
/// ```
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

/// Simula um pressionamento de tecla
///
/// # Parâmetros
///
/// * `key` - A tecla representada em uma string.
/// * `state` - O estado [`ButtonState`] da tecla (pressionado ou soltado).
///
/// # Exemplo
///
/// ```
/// process_key_press("a".to_string(), ButtonState::Press);
/// // Isso simulará o pressionamento da tecla 'a'
/// ```
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

/// Processa uma ação de entrada do usuário
///
/// # Parâmetros
///
/// * `action` - A ação [`InputAction`] a ser processada.
///
/// # Exemplo
///
/// ```
/// let action = InputAction::MouseMove { dx: 10, dy: 5 };
/// process_action(action);
/// // Isso moverá o mouse por (10, 5)
/// ```
pub fn process_action(action: InputAction) {
    match action {
        InputAction::MouseMove { dx, dy } => process_mouse_move(dx, dy),
        InputAction::MouseClick { button, state } => process_mouse_click(button, state),
        InputAction::MouseScroll { direction, amount } => process_scroll(direction, amount),
        InputAction::KeyPress { key, state } => process_key_press(key, state),
    }
}
