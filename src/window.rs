//! # Janela do QR code do Teclado Helena
//!
//! Módulo para gerar e exibir o QR code com o link para a interface do cliente,
//! usando winit (cross-platform, Wayland/X11/Windows/Mac).

use image::{GrayImage, Luma};
use qrcode_generator::{to_image, QrCodeEcc};
use softbuffer::{Context, Surface};
use std::num::NonZeroU32;
use winit::{
    application::ApplicationHandler,
    event::WindowEvent,
    event_loop::{ActiveEventLoop, EventLoop},
    window::{Window, WindowAttributes, WindowId},
};

use std::sync::Arc;

const WIN_W: u32 = 350;
const WIN_H: u32 = 380;
const QR_SIZE: u32 = 290;
const QR_X: u32 = 30;
const QR_Y: u32 = 20;

/// Gera QR code e retorna como pixels RGBA (width x height)
fn generate_qr_pixels(ip_url: &str) -> Vec<u8> {
    let img_raw = to_image(ip_url, QrCodeEcc::Low, QR_SIZE as usize).unwrap();
    // img_raw is grayscale L8, convert to RGBA
    let gray = GrayImage::from_raw(QR_SIZE, QR_SIZE, img_raw).unwrap();
    let mut rgba = Vec::with_capacity((QR_SIZE * QR_SIZE * 4) as usize);
    for Luma([l]) in gray.pixels() {
        rgba.push(*l); // R
        rgba.push(*l); // G
        rgba.push(*l); // B
        rgba.push(255); // A
    }
    rgba
}

struct App {
    ip: String,
    qr_pixels: Vec<u8>,
    window: Option<Arc<Window>>,
    surface: Option<Surface<Arc<Window>, Arc<Window>>>,
    context: Option<Context<Arc<Window>>>,
}

impl App {
    fn new(ip: String) -> Self {
        let qr_pixels = generate_qr_pixels(&ip);
        Self {
            ip,
            qr_pixels,
            window: None,
            surface: None,
            context: None,
        }
    }

    fn render(&mut self) {
        let surface = self.surface.as_mut().unwrap();
        let window = self.window.as_ref().unwrap();

        surface
            .resize(
                NonZeroU32::new(WIN_W).unwrap(),
                NonZeroU32::new(WIN_H).unwrap(),
            )
            .unwrap();

        let mut buf = surface.buffer_mut().unwrap();

        // Fill background white
        buf.fill(0xFFFFFFFF);

        // Draw QR code pixels into buffer
        for py in 0..QR_SIZE {
            for px in 0..QR_SIZE {
                let src = ((py * QR_SIZE + px) * 4) as usize;
                let r = self.qr_pixels[src] as u32;
                let g = self.qr_pixels[src + 1] as u32;
                let b = self.qr_pixels[src + 2] as u32;
                let color = (r << 16) | (g << 8) | b;

                let dx = (QR_X + px) as usize;
                let dy = (QR_Y + py) as usize;
                buf[dy * WIN_W as usize + dx] = color;
            }
        }

        // Draw IP text as simple pixel row (just marks the area — use a font crate for real text)
        // For a proper text label, add the `fontdue` or `ab_glyph` crate.
        // Here we draw a light gray bar as placeholder:
        let text_y = (QR_Y + QR_SIZE + 10) as usize;
        for row in text_y..(text_y + 24) {
            for col in 10..(WIN_W as usize - 10) {
                buf[row * WIN_W as usize + col] = 0x00F0F0F0;
            }
        }

        window.request_redraw();
        buf.present().unwrap();
    }
}

impl ApplicationHandler for App {
    fn resumed(&mut self, event_loop: &ActiveEventLoop) {
        let attrs = WindowAttributes::default()
            .with_title("Teclado Helena")
            .with_inner_size(winit::dpi::LogicalSize::new(WIN_W, WIN_H))
            .with_resizable(false);

        let window = Arc::new(event_loop.create_window(attrs).unwrap());
        let context = Context::new(Arc::clone(&window)).unwrap();
        let surface = Surface::new(&context, Arc::clone(&window)).unwrap();

        self.window = Some(window);
        self.context = Some(context);
        self.surface = Some(surface);
        self.render();
    }

    fn window_event(&mut self, event_loop: &ActiveEventLoop, _id: WindowId, event: WindowEvent) {
        match event {
            WindowEvent::RedrawRequested => self.render(),
            WindowEvent::CloseRequested => event_loop.exit(),
            _ => {}
        }
    }
}

/// Cria e exibe a janela com o QR code. Funciona em Wayland, X11, Windows e macOS.
pub fn create_window(ip: String) {
    let event_loop = EventLoop::new().unwrap();
    let mut app = App::new(ip);
    event_loop.run_app(&mut app).unwrap();
}
