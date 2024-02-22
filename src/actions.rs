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
    /// Representa uma ação de entrada de texto
    StringInput {
        /// O texto a ser inserido
        text: String,
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
/// * `enigo` - Uma referência mutável para um objeto [`Enigo`] para simular a ação.
///
/// # Exemplo
///
/// ```
/// let mut enigo = Enigo::new(&Settings::default()).unwrap();
/// process_mouse_move(10, 5, enigo);
/// // Isso moverá o mouse por (10, 5)
/// ```
pub fn process_mouse_move(dx: i32, dy: i32, enigo: &mut Enigo) {
    println!("Move mouse by ({}, {})", dx, dy);
    enigo.move_mouse(dx, dy, Coordinate::Rel).unwrap();
}

/// Simula uma ação de clique
///
/// # Parâmetros
///
/// * `button` - O botão [`MouseButton`] que foi clicado.
/// * `state` - O estado [`ButtonState`] do botão.
/// * `enigo` - Uma referência mutável para um objeto [`Enigo`] para simular a ação.
///
/// # Exemplo
///
/// ```
/// let mut enigo = Enigo::new(&Settings::default()).unwrap();
/// process_mouse_click(MouseButton::Left, ButtonState::Press, enigo);
/// // Isso simulará um pressionamento do botão esquerdo do mouse
/// 
/// process_mouse_click(MouseButton::Left, ButtonState::Release, enigo);
/// // Isso simulará um soltamento do botão esquerdo do mouse
/// ```
pub fn process_mouse_click(button: MouseButton, state: ButtonState, enigo: &mut Enigo) {
    match (button, state) {
        (MouseButton::Left, ButtonState::Press) => {
            println!("Press left button");
            enigo.button(enigo::Button::Left, enigo::Direction::Press).unwrap();
        }
        (MouseButton::Left, ButtonState::Release) => {
            println!("Release left button");
            enigo.button(enigo::Button::Left, enigo::Direction::Release).unwrap();
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
/// * `enigo` - Uma referência mutável para um objeto [`Enigo`] para simular a ação.
///
/// # Exemplo
///
/// ```
/// let mut enigo = Enigo::new(&Settings::default()).unwrap();
/// process_scroll(ScrollDirection::Up, 3, enigo);
/// // Isso simulará uma rolagem da roda de mouse para cima de 3 unidades
/// ```
pub fn process_scroll(direction: ScrollDirection, amount: i32, enigo: &mut Enigo) {
    match direction {
        ScrollDirection::Up => {
            println!("Scroll up by {}", amount);
            enigo.scroll(-amount, enigo::Axis::Vertical).unwrap();
        }
        ScrollDirection::Down => {
            println!("Scroll down by {}", amount);
            enigo.scroll(amount, enigo::Axis::Vertical).unwrap();
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
fn get_key(key: String) -> Result<enigo::Key, &'static str> {
    match key.as_str() {
        "esc" => Ok(enigo::Key::Escape),
        "1" => Ok(enigo::Key::Num1),
        "2" => Ok(enigo::Key::Num2),
        "3" => Ok(enigo::Key::Num3),
        "4" => Ok(enigo::Key::Num4),
        "5" => Ok(enigo::Key::Num5),
        "6" => Ok(enigo::Key::Num6),
        "7" => Ok(enigo::Key::Num7),
        "8" => Ok(enigo::Key::Num8),
        "9" => Ok(enigo::Key::Num9),
        "0" => Ok(enigo::Key::Num0),
        "minus" => Ok(enigo::Key::Unicode('-')),
        "equals" => Ok(enigo::Key::Unicode('=')),
        "quote" => Ok(enigo::Key::Unicode('\'')),
        "slash" => Ok(enigo::Key::AbntC1),
        "semicolon" => Ok(enigo::Key::Unicode(';')),
        "comma" => Ok(enigo::Key::Unicode(',')),
        "period" => Ok(enigo::Key::Unicode('.')),
        "tilde" => Ok(enigo::Key::Unicode('~')),
        "acute" => Ok(enigo::Key::Unicode('´')),
        "delete" => Ok(enigo::Key::Delete),
        "backspace" => Ok(enigo::Key::Backspace),
        "q" => Ok(enigo::Key::Q),
        "w" => Ok(enigo::Key::W),
        "e" => Ok(enigo::Key::E),
        "r" => Ok(enigo::Key::R),
        "t" => Ok(enigo::Key::T),
        "y" => Ok(enigo::Key::Y),
        "u" => Ok(enigo::Key::U),
        "i" => Ok(enigo::Key::I),
        "o" => Ok(enigo::Key::O),
        "p" => Ok(enigo::Key::P),
        "a" => Ok(enigo::Key::A),
        "s" => Ok(enigo::Key::S),
        "d" => Ok(enigo::Key::D),
        "f" => Ok(enigo::Key::F),
        "g" => Ok(enigo::Key::G),
        "h" => Ok(enigo::Key::H),
        "j" => Ok(enigo::Key::J),
        "k" => Ok(enigo::Key::K),
        "l" => Ok(enigo::Key::L),
        "enter" => Ok(enigo::Key::Return),
        "z" => Ok(enigo::Key::Z),
        "x" => Ok(enigo::Key::X),
        "c" => Ok(enigo::Key::C),
        "v" => Ok(enigo::Key::V),
        "b" => Ok(enigo::Key::B),
        "n" => Ok(enigo::Key::N),
        "m" => Ok(enigo::Key::M),
        "up-arrow" => Ok(enigo::Key::UpArrow),
        "ctrl" => Ok(enigo::Key::Control),
        "alt" => Ok(enigo::Key::Alt),
        "space" => Ok(enigo::Key::Space),
        "left-arrow" => Ok(enigo::Key::LeftArrow),
        "home" => Ok(enigo::Key::Home),
        "down-arrow" => Ok(enigo::Key::DownArrow),
        "right-arrow" => Ok(enigo::Key::RightArrow),
        "end" => Ok(enigo::Key::End),
        "cedilla" => Ok(enigo::Key::OEM1),
        "shift" => Ok(enigo::Key::Shift),
        _ => Err("Unknown key"),
    }
}

/// Simula um pressionamento de tecla
///
/// # Parâmetros
///
/// * `key` - A tecla representada em uma string.
/// * `state` - O estado [`ButtonState`] da tecla (pressionado ou soltado).
/// * `enigo` - Uma referência mutável para um objeto [`Enigo`] para simular a ação.
///
/// # Exemplo
///
/// ```
/// let mut enigo = Enigo::new(&Settings::default()).unwrap();
/// process_key_press("a".to_string(), ButtonState::Press, enigo);
/// // Isso simulará o pressionamento da tecla 'a'
/// ```
pub fn process_key_press(key: String, state: ButtonState, enigo: &mut Enigo) {
    match state {
        ButtonState::Press => {
            println!("Press {:?} key", key);
            if let Ok(key) = get_key(key) {
                enigo.key(key, enigo::Direction::Press).unwrap();
            } else {
                println!("Unknown key");
            }
        }
        ButtonState::Release => {
            println!("Release {:?} key", key);
            if let Ok(key) = get_key(key) {
                enigo.key(key, enigo::Direction::Release).unwrap();
            } else {
                println!("Unknown key");
            }
        }
    }
}

/// Simula uma inserção de texto
///
/// # Parâmetros
/// * `text` - O texto a ser inserido.
///
/// # Exemplo
///
/// ```
/// let mut enigo = Enigo::new(&Settings::default()).unwrap();
/// process_string_input("Hello, World!".to_string(), enigo);
/// ```
pub fn process_string_input(text: String, enigo: &mut Enigo) {
    println!("Input text: {}", text);
    enigo.text(&text).unwrap();
}

/// Processa uma ação de entrada do usuário
///
/// # Parâmetros
///
/// * `action` - A ação [`InputAction`] a ser processada.
/// * `enigo` - Uma referência mutável para um objeto [`Enigo`] para simular a ação.
///
/// # Exemplo
///
/// ```
/// let mut enigo = Enigo::new(&Settings::default()).unwrap();
/// let action = InputAction::MouseMove { dx: 10, dy: 5 };
/// process_action(action, enigo);
/// // Isso moverá o mouse por (10, 5)
/// ```
pub fn process_action(action: InputAction, enigo: &mut Enigo) {
    match action {
        InputAction::MouseMove { dx, dy } => process_mouse_move(dx, dy, enigo),
        InputAction::MouseClick { button, state } => process_mouse_click(button, state, enigo),
        InputAction::MouseScroll { direction, amount } => process_scroll(direction, amount, enigo),
        InputAction::KeyPress { key, state } => process_key_press(key, state, enigo),
        InputAction::StringInput { text } => process_string_input(text, enigo),
    }
}
