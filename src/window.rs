//! # Janela do QR code do Teclado Helena
//!
//! Módulo para gerar e exibir o QR code com o link para a interface do cliente, utilizando a API Win32.

use image::{codecs::bmp::BmpEncoder, ColorType, ImageEncoder};
use qrcode_generator::{to_image, QrCodeEcc};
use windows_sys::{
    w,
    Win32::{
        Foundation::{HWND, LPARAM, LRESULT, RECT, WPARAM},
        Graphics::Gdi::*,
        System::{
            LibraryLoader::GetModuleHandleW,
            SystemServices::{SS_BITMAP, SS_CENTER},
        },
        UI::WindowsAndMessaging::*,
    },
};

/// Gera um QR code a partir de um `ip_url`, retornando uma handle para a imagem bitmap (HBITMAP).
///
/// # Parâmetros
///
/// * `ip_url` - Uma string slice contendo a URL ou texto a ser codificada em um QR code.
///
/// # Pânico
///
/// Esta função entra em pânico se a geração do QR code falhar ou se a codificação do BMP falhar.
///
/// # Retorno
///
/// Uma handle para o bitmap criado (HBITMAP) que representa o QR code.
fn generate_qr_code(ip_url: &str) -> HBITMAP {
    let mut bmp = Vec::with_capacity(4096);
    let img_raw = to_image(&ip_url, QrCodeEcc::Low, 290).unwrap();
    let encoder = BmpEncoder::new(&mut bmp);
    encoder
        .write_image(&img_raw, 290, 290, ColorType::L8)
        .unwrap();
    let hbitmap = create_hbitmap_from_vec(&bmp);
    hbitmap
}

/// Cria um device-independent bitmap (DIB) a partir do vetor de dados bitmap.
///
/// # Parâmetros
///
/// * `bitmap_data` - Um vetor contendo dados de arquivo BMP.
///
/// # Segurança
///
/// Esta função utiliza blocos unsafe para interagir com a API Win32 e realiza conversões de ponteiro bruto
/// O chamador deve garantir que os dados bitmap sejam uma imagem BMP válida.
///
/// # Retorno
///
/// Uma handle para o bitmap criado (HBITMAP) que representa os dados da imagem carregada.
fn create_hbitmap_from_vec(bitmap_data: &Vec<u8>) -> HBITMAP {
    let bitmap_file_header: &BITMAPFILEHEADER = unsafe { std::mem::transmute(&bitmap_data[0]) };
    let bmi: &BITMAPINFO =
        unsafe { std::mem::transmute(&bitmap_data[std::mem::size_of::<BITMAPFILEHEADER>()]) };

    let pixel_data_offset = bitmap_file_header.bfOffBits;
    let pixel_data: &[u8] = &bitmap_data[pixel_data_offset as usize..];

    let hdc = unsafe { GetDC(0) };

    let hbitmap;
    unsafe {
        hbitmap = CreateDIBitmap(
            hdc,
            &bmi.bmiHeader,
            CBM_INIT.try_into().unwrap(),
            pixel_data.as_ptr() as *const _,
            &*bmi,
            DIB_RGB_COLORS,
        );
        ReleaseDC(0, hdc);
    }

    hbitmap
}

/// Carrega um recurso de ícone do executável atual e returna uma handle para o ícone (HICON).
///
/// # Segurança
///
/// Esta função utiliza um bloco unsafe para chamar a API do Windows.
///
/// # Retorno
///
/// Uma handle para o ícone carregado (HICON).
fn load_icon() -> HICON {
    let icon = unsafe {
        LoadImageW(
            GetModuleHandleW(std::ptr::null()),
            1usize as *const u16,
            IMAGE_ICON,
            0,
            0,
            LR_DEFAULTSIZE,
        )
    };
    icon
}

/// Cria e exibe uma janela que contém a imagem do QR code gerado a partir da string `ip` fornecida
/// e exibe o endereço de IP.
///
/// # Parâmetros
///
/// * `ip` - Uma string representando um endereço de IP em URL, que será exibida e codificada em um QR code.
///
/// # Segurança
///
/// Esta função utiliza blocos unsafe para criar, manipular e interagir com as janelas da API Win32.

pub fn create_window(ip: String) {
    let image = generate_qr_code(&ip);
    unsafe {
        let instance = GetModuleHandleW(std::ptr::null());
        debug_assert!(instance != 0);

        let mut rect = RECT {
            left: 0,
            top: 0,
            right: 350,
            bottom: 350,
        };
        AdjustWindowRect(&mut rect as *mut RECT, WS_OVERLAPPEDWINDOW, 0);

        let wc = WNDCLASSEXW {
            cbSize: std::mem::size_of::<WNDCLASSEXW>() as u32,
            hCursor: LoadCursorW(0, IDC_ARROW),
            hInstance: instance,
            lpszClassName: w!("window"),
            style: CS_HREDRAW | CS_VREDRAW,
            lpfnWndProc: Some(wndproc),
            cbClsExtra: 0,
            cbWndExtra: 0,
            hIcon: load_icon(),
            hIconSm: load_icon(),
            hbrBackground: 0,
            lpszMenuName: std::ptr::null(),
        };

        let atom = RegisterClassExW(&wc);
        debug_assert!(atom != 0);

        let hwnd = CreateWindowExW(
            0,
            w!("window"),
            w!("Teclado Helena"),
            WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX | WS_VISIBLE,
            CW_USEDEFAULT,
            CW_USEDEFAULT,
            rect.right - rect.left,
            rect.bottom - rect.top,
            0,
            0,
            instance,
            std::ptr::null(),
        );

        let image_hwnd = CreateWindowExW(
            WINDOW_EX_STYLE::default(),
            w!("static"),
            w!("QR Code"),
            WS_VISIBLE | WS_CHILD | SS_BITMAP,
            30,
            20,
            290,
            290,
            hwnd,
            0,
            0,
            std::ptr::null(),
        );

        SendMessageW(
            image_hwnd,
            STM_SETIMAGE,
            IMAGE_BITMAP.try_into().unwrap(),
            image,
        );

        let ip_text_control = CreateWindowExW(
            WINDOW_EX_STYLE::default(),
            w!("static"),
            w!("IP Local"),
            WS_VISIBLE | WS_CHILD | SS_CENTER,
            0,
            312,
            350,
            30,
            hwnd,
            0,
            0,
            std::ptr::null(),
        );

        let mut v: Vec<u16> = ip.encode_utf16().collect();
        v.push(0);

        SetWindowTextW(ip_text_control, v.as_ptr());

        let mut message = std::mem::zeroed();

        while GetMessageW(&mut message, 0, 0, 0) != 0 {
            DispatchMessageW(&message);
        }
    }
}

/// Uma função de retorno de chamada que processa mensagens enviadas para uma janela.
///
/// Documentação Win32: <https://learn.microsoft.com/pt-br/windows/win32/api/winuser/nc-winuser-wndproc>
extern "system" fn wndproc(window: HWND, message: u32, wparam: WPARAM, lparam: LPARAM) -> LRESULT {
    unsafe {
        match message {
            WM_PAINT => {
                println!("WM_PAINT");
                ValidateRect(window, std::ptr::null());
                0
            }
            WM_DESTROY => {
                println!("WM_DESTROY");
                PostQuitMessage(0);
                0
            }
            WM_CTLCOLORSTATIC => {
                println!("WM_CTLCOLORSTATIC");
                let hdc_static = wparam as HDC;
                SetTextColor(hdc_static, 0x00000000);
                SetBkColor(hdc_static, 0x00FFFFFF);

                let hbr_bkgnd = CreateSolidBrush(0x00FFFFFF);
                hbr_bkgnd as LRESULT
            }
            _ => DefWindowProcW(window, message, wparam, lparam),
        }
    }
}
