package logica;

import java.io.*;
import java.util.*;

import modelo.Mesa;
import modelo.Prestamo;
import modelo.cafeteria.Bebida;
import modelo.cafeteria.Pasteleria;
import modelo.cafeteria.ProductoCafeteria;
import modelo.juegos.Copia;
import modelo.juegos.Juego;
import modelo.turnos.Turno;
import modelo.turnos.SolicitudCambioTurno;
import modelo.usuarios.*;
import modelo.ventas.ItemVenta;
import modelo.ventas.Venta;

public class BoardGameCafe {
    private Map<String, Usuario> usuarios;
    private Map<String, Juego> juegos;
    private Map<String, Copia> copias;
    private Map<String, ProductoCafeteria> productosMenu;
    private List<Prestamo> prestamos;
    private List<Venta> ventas;
    private List<SolicitudCambioTurno> solicitudesTurno;

    public BoardGameCafe() {
        usuarios = new HashMap<>();
        juegos = new HashMap<>();
        copias = new HashMap<>();
        productosMenu = new HashMap<>();
        prestamos = new ArrayList<>();
        ventas = new ArrayList<>();
        solicitudesTurno = new ArrayList<>();
    }

    // --------------------------------------------------------
    // MÉTODOS DE PERSISTENCIA (TXT BÁSICO)
    // --------------------------------------------------------
    public void guardarDatos() {
        File dir = new File("../datos");
        if (!dir.exists()) dir.mkdirs();
        
        try (PrintWriter pw = new PrintWriter(new File(dir, "usuarios.txt"))) {
            for (Usuario u : usuarios.values()) {
                String tipo = u.getClass().getSimpleName();
                pw.println(tipo + ";" + u.getLogin() + ";" + u.getPassword());
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (PrintWriter pw = new PrintWriter(new File(dir, "juegos.txt"))) {
            for (Juego j : juegos.values()) {
                pw.println(j.getNombre() + ";" + j.getEmpresa() + ";" + j.getAnioPublicacion() + ";" +
                           j.getMinJugadores() + ";" + j.getMaxJugadores() + ";" + j.getMinEdad() + ";" +
                           j.getCategoria() + ";" + j.isEsDificil() + ";" + j.getPrecioVenta());
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (PrintWriter pw = new PrintWriter(new File(dir, "copias.txt"))) {
            for (Copia c : copias.values()) {
                pw.println(c.getId() + ";" + c.getJuego().getNombre() + ";" + c.getEstado() + ";" +
                           c.getInventario() + ";" + c.isDisponible());
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        try (PrintWriter pw = new PrintWriter(new File(dir, "ventas.txt"))) {
            for (Venta v : ventas) {
                pw.println(v.getFecha() + ";" + v.getTotalFinal());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        try (PrintWriter pw = new PrintWriter(new File(dir, "prestamos.txt"))) {
            for (Prestamo p : prestamos) {
                pw.println(p.getFechaPrestamo() + ";" + p.getFechaDevolucion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public void cargarDatos() throws Exception {
        File dir = new File("../datos");
        if (!dir.exists()) return;

        File fUsuarios = new File(dir, "usuarios.txt");
        if (fUsuarios.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fUsuarios));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String tipo = partes[0];
                String login = partes[1];
                String pass = partes[2];
                Usuario u = null;
                if (tipo.equals("Administrador")) u = new Administrador(login, pass);
                else if (tipo.equals("Cliente")) u = new Cliente(login, pass, 0);
                else if (tipo.equals("Mesero")) u = new Mesero(login, pass, login + "123");
                else if (tipo.equals("Cocinero")) u = new Cocinero(login, pass, login + "123");
                
                if (u != null) usuarios.put(login, u);
            }
            br.close();
        }

        File fJuegos = new File(dir, "juegos.txt");
        if (fJuegos.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fJuegos));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                Juego j = new Juego(p[0], p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3]),
                                    Integer.parseInt(p[4]), Integer.parseInt(p[5]), p[6],
                                    Boolean.parseBoolean(p[7]), Double.parseDouble(p[8]));
                juegos.put(j.getNombre(), j);
            }
            br.close();
        }

        File fCopias = new File(dir, "copias.txt");
        if (fCopias.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fCopias));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                Juego j = juegos.get(p[1]);
                if (j != null) {
                    Copia c = new Copia(p[0], j, p[2], p[3], Boolean.parseBoolean(p[4]));
                    copias.put(c.getId(), c);
                }
            }
            br.close();
        }
        
    }

    // --------------------------------------------------------
    // ACÁ METEMOS MÉTODOS DE NEGOCIO (LOGICA PRINCIPAL)
    // --------------------------------------------------------

    public Map<String, Usuario> getUsuarios() { return usuarios; }
    public Map<String, Copia> getCopias() { return copias; }

    public void registrarUsuario(Usuario u) {
        usuarios.put(u.getLogin(), u);
    }

    public void agregarJuego(Juego j) { juegos.put(j.getNombre(), j); }
    public void agregarCopia(Copia c) { copias.put(c.getId(), c); }
    public void agregarProductoMenu(ProductoCafeteria p) { productosMenu.put(p.getNombre(), p); }

    public void realizarPrestamo(UsuarioComprador usuario, Mesa mesa, List<Copia> copiasPedidas, Mesero meseroAcompaniante) throws Exception {
    	if (usuario instanceof Empleado) {
    	    Empleado emp = (Empleado) usuario;
    	    if (emp.estaEnTurno()) {
    	        throw new Exception("Un empleado en turno no puede solicitar prestamos.");
    	    }
    	}
        if (usuario.getPrestamosActuales().size() + copiasPedidas.size() > 2) {
            throw new Exception("ERROR: Un cliente no puede tener mas de 2 juegos prestados a la vez.");
        }

        boolean tieneJuegoAccion = false;

        for (Copia c : copiasPedidas) {
            Juego j = c.getJuego();

            if (j.getCategoria().equals("Accion")) {
                tieneJuegoAccion = true;
            }

            if (!c.isDisponible() || !c.getInventario().equals("Prestamo")) {
                throw new Exception("ERROR: La copia " + c.getId() + " no esta disponible para prestamo.");
            }

            if (mesa != null) {
                if (mesa.getNumPersonas() < j.getMinJugadores() || mesa.getNumPersonas() > j.getMaxJugadores()) {
                    throw new Exception("ERROR: Restriccion de numero de jugadores para " + j.getNombre());
                }
                if (j.getMinEdad() >= 18 && (mesa.isHayMenores18() || mesa.isHayMenores5())) {
                    throw new Exception("ERROR: Juego exclusivo para adultos. Hay menores en la mesa.");
                }
                if (j.getMinEdad() > 5 && mesa.isHayMenores5()) {
                    throw new Exception("ERROR: Juego no apto para menores de 5 años.");
                }
            }

            if (j.isEsDificil()) {
                if (meseroAcompaniante == null) {
                    System.out.println("ADVERTENCIA: Han pedido un juego dificil sin mesero introductor.");
                } else if (!meseroAcompaniante.getJuegosQueConoce().contains(j)) {
                    System.out.println("ADVERTENCIA: El mesero no conoce el juego, puede que no logren entenderlo.");
                }
            }
        }

        if (tieneJuegoAccion && mesa != null) {
            throw new Exception("No se puede prestar juego de accion con bebidas calientes en la mesa.");
        }
        
        
        // Bloqueo adicional por bebida caliente si es accion y ya pidieron bebida. (Verificable a nivel de compras conjuntas).

        Prestamo p = new Prestamo(copiasPedidas, usuario, mesa, new java.util.Date().toString(), meseroAcompaniante);
        for (Copia c : copiasPedidas) {
            c.setDisponible(false);
            usuario.agregarPrestamo(c);
        }
        prestamos.add(p);
    }
    
    public void venderProductos(UsuarioComprador comprador, List<ItemVenta> items, Mesa mesaAtendida) throws Exception {
        double subtotal = 0;
        double impuestos = 0;
        
        for (ItemVenta item : items) {
            Object obj = item.getItem();
            if (obj instanceof Copia) {
                Copia c = (Copia) obj;
                if (!c.getInventario().equals("Venta") || !c.isDisponible()) {
                    throw new Exception("Copia " + c.getId() + " no disponible para venta.");
                }
                c.setDisponible(false);
                subtotal += item.getSubtotal();
                impuestos += item.getSubtotal() * 0.19; // IVA
            } else if (obj instanceof ProductoCafeteria) {
                ProductoCafeteria p = (ProductoCafeteria) obj;
                if (p instanceof Bebida) {
                    Bebida b = (Bebida) p;
                    if (b.isEsAlcoholica() && mesaAtendida != null && (mesaAtendida.isHayMenores18() || mesaAtendida.isHayMenores5())) {
                        throw new Exception("ERROR: No se puede vender alcohol a mesas con menores.");
                    }
                    if (b.isEsCaliente() && tieneMesaJuegoAccion(comprador)) {
                        throw new Exception("ERROR: No se pueden despachar bebidas calientes si hay juego de Accion prestado.");
                    }
                } else if (p instanceof Pasteleria) {
                    Pasteleria pas = (Pasteleria) p;
                    if (!pas.getAlergenos().isEmpty()) {
                        System.out.println("ATENCION INFO ALERGENOS: " + String.join(", ", pas.getAlergenos()));
                    }
                }
                subtotal += item.getSubtotal();
                impuestos += item.getSubtotal() * 0.08; // Impoconsumo
            }
        }
        
        double descuento = 0;
        if (comprador instanceof Empleado) {
            descuento = subtotal * 0.20;
        } else if (comprador instanceof Cliente) {
            Cliente cl = (Cliente) comprador;
            descuento = cl.getPuntosFidelidad(); // uso basico de puntos
            cl.restarPuntosFidelidad((int)descuento);
        }
        
        double propina = subtotal * 0.10;
        int puntosGenerados = (int)((subtotal + impuestos + propina - descuento) * 0.01);
        
        if (comprador instanceof Cliente) {
            ((Cliente) comprador).agregarPuntosFidelidad(puntosGenerados);
        }
        
        Venta v = new Venta(items, comprador, impuestos, propina, subtotal, descuento, puntosGenerados, new java.util.Date().toString());
        comprador.registrarCompra(v);
        ventas.add(v);
    }
    
    private boolean tieneMesaJuegoAccion(UsuarioComprador u) {
        for (Copia c : u.getPrestamosActuales()) {
            if (c.getJuego().getCategoria().equals("Accion")) return true;
        }
        return false;
    }
    
    public void cambiarTurno(SolicitudCambioTurno solicitud, Administrador admin) throws Exception {

        // Creamos una validación antes de todo
        if (!cumpleMinimoPersonal()) {
            throw new Exception("No se puede aprobar el cambio de turno. No se cumple el minimo de empleados.");
        }

        solicitud.aprobar();

        if (solicitud.getReemplazo() != null) {
            Turno temp = solicitud.getSolicitante().getTurno();
            solicitud.getSolicitante().setTurno(solicitud.getReemplazo().getTurno());
            solicitud.getReemplazo().setTurno(temp);
        }
    }

    public List<Venta> getVentas() { return ventas; }
    
    
    private boolean cumpleMinimoPersonal() {
        int cocineros = 0;
        int meseros = 0;

        for (Usuario u : usuarios.values()) {
            if (u instanceof Cocinero) cocineros++;
            if (u instanceof Mesero) meseros++;
        }

        return cocineros >= 1 && meseros >= 2;
    }
}
