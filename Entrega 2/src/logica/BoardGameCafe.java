package logica;

import java.io.*;
import java.util.*;

import modelo.Mesa;
import modelo.Prestamo;
import modelo.Reserva;
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
import modelo.torneos.Torneo;
import modelo.torneos.TorneoAmistoso;
import modelo.torneos.TorneoCompetitivo;
import modelo.torneos.Inscripcion;
import org.json.JSONArray;
import org.json.JSONObject;

public class BoardGameCafe {
    private Map<String, Usuario> usuarios;
    private Map<String, Juego> juegos;
    private Map<String, Copia> copias;
    private Map<String, ProductoCafeteria> productosMenu;
    private List<Prestamo> prestamos;
    private List<Venta> ventas;
    private List<SolicitudCambioTurno> solicitudesTurno;
    private List<Torneo> torneos;
    private List<Reserva> reservas;

    public BoardGameCafe() {
        usuarios = new HashMap<>();
        juegos = new HashMap<>();
        copias = new HashMap<>();
        productosMenu = new HashMap<>();
        prestamos = new ArrayList<>();
        ventas = new ArrayList<>();
        solicitudesTurno = new ArrayList<>();
        torneos = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    // --------------------------------------------------------
    // PERSISTENCIA COMPLETA
    // --------------------------------------------------------

    public void guardarDatos() {
        File dir = new File("datos");
        if (!dir.exists()) dir.mkdirs();

        // usuarios.txt
        try (PrintWriter pw = new PrintWriter(new File(dir, "usuarios.txt"))) {
            for (Usuario u : usuarios.values()) {
                if (u instanceof Administrador) {
                    pw.println("Administrador;" + u.getLogin() + ";" + u.getPassword());
                } else if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    pw.println("Cliente;" + c.getLogin() + ";" + c.getPassword() + ";" + c.getPuntosFidelidad());
                } else if (u instanceof Mesero) {
                    Mesero m = (Mesero) u;
                    String dia = (m.getTurno() != null) ? m.getTurno().getDia() : "";
                    String horario = (m.getTurno() != null) ? m.getTurno().getHorario() : "";
                    pw.println("Mesero;" + m.getLogin() + ";" + m.getPassword() + ";"
                            + m.getCodigoDescuento() + ";" + dia + ";" + horario);
                } else if (u instanceof Cocinero) {
                    Cocinero c = (Cocinero) u;
                    String dia = (c.getTurno() != null) ? c.getTurno().getDia() : "";
                    String horario = (c.getTurno() != null) ? c.getTurno().getHorario() : "";
                    pw.println("Cocinero;" + c.getLogin() + ";" + c.getPassword() + ";"
                            + c.getCodigoDescuento() + ";" + dia + ";" + horario);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        // juegos.txt
        try (PrintWriter pw = new PrintWriter(new File(dir, "juegos.txt"))) {
            for (Juego j : juegos.values()) {
                pw.println(j.getNombre() + ";" + j.getEmpresa() + ";" + j.getAnioPublicacion() + ";"
                        + j.getMinJugadores() + ";" + j.getMaxJugadores() + ";" + j.getMinEdad() + ";"
                        + j.getCategoria() + ";" + j.isEsDificil() + ";" + j.getPrecioVenta());
            }
        } catch (Exception e) { e.printStackTrace(); }

        // copias.txt
        try (PrintWriter pw = new PrintWriter(new File(dir, "copias.txt"))) {
            for (Copia c : copias.values()) {
                pw.println(c.getId() + ";" + c.getJuego().getNombre() + ";" + c.getEstado() + ";"
                        + c.getInventario() + ";" + c.isDisponible());
            }
        } catch (Exception e) { e.printStackTrace(); }

        // menu.txt
        try (PrintWriter pw = new PrintWriter(new File(dir, "menu.txt"))) {
            for (ProductoCafeteria p : productosMenu.values()) {
                if (p instanceof Bebida) {
                    Bebida b = (Bebida) p;
                    pw.println("Bebida;" + b.getNombre() + ";" + b.getPrecio() + ";"
                            + b.isEsAlcoholica() + ";" + b.isEsCaliente());
                } else if (p instanceof Pasteleria) {
                    Pasteleria pas = (Pasteleria) p;
                    String alergenos = String.join(",", pas.getAlergenos());
                    pw.println("Pasteleria;" + pas.getNombre() + ";" + pas.getPrecio() + ";" + alergenos);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        // ventas.txt — incluye subtotalCafeteria y subtotalJuegos
        // Formato: fecha;login;subtotal;impuestos;propina;descuento;puntos;subtotalCafe;subtotalJuegos
        try (PrintWriter pw = new PrintWriter(new File(dir, "ventas.txt"))) {
            for (Venta v : ventas) {
                pw.println(v.getFecha() + ";" + v.getComprador().getLogin() + ";"
                        + v.getSubtotal() + ";" + v.getImpuestos() + ";"
                        + v.getPropina() + ";" + v.getDescuentoAplicado() + ";" + v.getPuntosGenerados() + ";"
                        + v.getSubtotalCafeteria() + ";" + v.getSubtotalJuegos());
            }
        } catch (Exception e) { e.printStackTrace(); }

        // prestamos.txt
        try (PrintWriter pw = new PrintWriter(new File(dir, "prestamos.txt"))) {
            for (Prestamo p : prestamos) {
                String copiaIds = "";
                for (Copia c : p.getCopias()) {
                    if (!copiaIds.isEmpty()) copiaIds += ",";
                    copiaIds += c.getId();
                }
                String fechaDev = (p.getFechaDevolucion() != null) ? p.getFechaDevolucion() : "null";
                pw.println(p.getFechaPrestamo() + ";" + fechaDev + ";"
                        + p.getUsuario().getLogin() + ";" + copiaIds);
            }
        } catch (Exception e) { e.printStackTrace(); }

        // reservas.txt
        try (PrintWriter pw = new PrintWriter(new File(dir, "reservas.txt"))) {
            for (Reserva r : reservas) {
                String login = r.getCliente() != null ? r.getCliente().getLogin() : "";
                int numMesa = r.getMesa() != null ? r.getMesa().getNumero() : -1;
                pw.println(r.getFecha() + ";" + r.getDiaSemana() + ";" + login + ";" + numMesa + ";" + r.getHora());
            }
        } catch (Exception e) { e.printStackTrace(); }

        guardarTorneosJSON();
    }

    public void cargarDatos() throws Exception {
        File dir = new File("datos");
        if (!dir.exists()) return;

        // usuarios.txt
        File fUsuarios = new File(dir, "usuarios.txt");
        if (fUsuarios.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fUsuarios));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                String tipo  = p[0];
                String login = p[1];
                String pass  = p[2];
                Usuario u = null;
                if (tipo.equals("Administrador")) {
                    u = new Administrador(login, pass);
                } else if (tipo.equals("Cliente")) {
                    int puntos = (p.length > 3) ? Integer.parseInt(p[3]) : 0;
                    u = new Cliente(login, pass, puntos);
                } else if (tipo.equals("Mesero")) {
                    String codigo = (p.length > 3) ? p[3] : login + "123";
                    Mesero m = new Mesero(login, pass, codigo);
                    if (p.length > 5 && !p[4].isEmpty()) m.setTurno(new Turno(p[4], p[5]));
                    u = m;
                } else if (tipo.equals("Cocinero")) {
                    String codigo = (p.length > 3) ? p[3] : login + "123";
                    Cocinero c = new Cocinero(login, pass, codigo);
                    if (p.length > 5 && !p[4].isEmpty()) c.setTurno(new Turno(p[4], p[5]));
                    u = c;
                }
                if (u != null) usuarios.put(login, u);
            }
            br.close();
        }

        // juegos.txt
        File fJuegos = new File(dir, "juegos.txt");
        if (fJuegos.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fJuegos));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                Juego j = new Juego(p[0], p[1], Integer.parseInt(p[2]),
                        Integer.parseInt(p[3]), Integer.parseInt(p[4]),
                        Integer.parseInt(p[5]), p[6],
                        Boolean.parseBoolean(p[7]), Double.parseDouble(p[8]));
                juegos.put(j.getNombre(), j);
            }
            br.close();
        }

        // copias.txt
        File fCopias = new File(dir, "copias.txt");
        if (fCopias.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fCopias));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                Juego j = juegos.get(p[1]);
                if (j != null) {
                    Copia c = new Copia(p[0], j, p[2], p[3], Boolean.parseBoolean(p[4]));
                    copias.put(c.getId(), c);
                }
            }
            br.close();
        }

        // menu.txt
        File fMenu = new File(dir, "menu.txt");
        if (fMenu.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fMenu));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p[0].equals("Bebida")) {
                    Bebida b = new Bebida(p[1], Double.parseDouble(p[2]),
                            Boolean.parseBoolean(p[3]), Boolean.parseBoolean(p[4]));
                    productosMenu.put(b.getNombre(), b);
                } else if (p[0].equals("Pasteleria")) {
                    List<String> alergenos = new ArrayList<>();
                    if (p.length > 3 && !p[3].isEmpty()) alergenos = Arrays.asList(p[3].split(","));
                    Pasteleria pas = new Pasteleria(p[1], Double.parseDouble(p[2]), alergenos);
                    productosMenu.put(pas.getNombre(), pas);
                }
            }
            br.close();
        }

        // ventas.txt — soporta formato antiguo (7 campos) y nuevo (9 campos)
        File fVentas = new File(dir, "ventas.txt");
        if (fVentas.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fVentas));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length >= 7) {
                    String fecha    = p[0];
                    String loginC   = p[1];
                    double subtotal = Double.parseDouble(p[2]);
                    double imp      = Double.parseDouble(p[3]);
                    double prop     = Double.parseDouble(p[4]);
                    double desc     = Double.parseDouble(p[5]);
                    int puntos      = Integer.parseInt(p[6]);
                    double subCafe  = (p.length > 7) ? Double.parseDouble(p[7]) : 0;
                    double subJuego = (p.length > 8) ? Double.parseDouble(p[8]) : 0;
                    Usuario u = usuarios.get(loginC);
                    if (u instanceof UsuarioComprador) {
                        Venta v = new Venta(new ArrayList<>(), (UsuarioComprador) u,
                                imp, prop, subtotal, desc, puntos, fecha, subCafe, subJuego);
                        ventas.add(v);
                    }
                }
            }
            br.close();
        }

        // prestamos.txt
        File fPrestamos = new File(dir, "prestamos.txt");
        if (fPrestamos.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fPrestamos));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length >= 4) {
                    String fechaP   = p[0];
                    String fechaDev = p[1].equals("null") ? null : p[1];
                    String loginU   = p[2];
                    String[] ids    = p[3].split(",");
                    Usuario u = usuarios.get(loginU);
                    if (u instanceof UsuarioComprador) {
                        List<Copia> lista = new ArrayList<>();
                        for (String id : ids) {
                            Copia c = copias.get(id.trim());
                            if (c != null) lista.add(c);
                        }
                        if (!lista.isEmpty()) {
                            Prestamo pr = new Prestamo(lista, (UsuarioComprador) u, null, fechaP, null);
                            pr.setFechaDevolucion(fechaDev);
                            prestamos.add(pr);
                        }
                    }
                }
            }
            br.close();
        }

        // reservas.txt
        File fReservas = new File(dir, "reservas.txt");
        if (fReservas.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(fReservas));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length >= 5) {
                    Usuario u = usuarios.get(p[2]);
                    if (u instanceof Cliente) {
                        Mesa mesa = new Mesa(Integer.parseInt(p[3]), 4, false, false);
                        Reserva r = new Reserva(p[0], p[1], (Cliente) u, mesa, p[4]);
                        reservas.add(r);
                    }
                }
            }
            br.close();
        }

        cargarTorneosJSON();
    }

    // --------------------------------------------------------
    // GETTERS Y MÉTODOS DE NEGOCIO
    // --------------------------------------------------------

    public Map<String, Usuario> getUsuarios()            { return usuarios; }
    public Map<String, Copia> getCopias()                { return copias; }
    public Map<String, Juego> getJuegos()                { return juegos; }
    public Map<String, ProductoCafeteria> getMenu()      { return productosMenu; }
    public List<Torneo> getTorneos()                     { return torneos; }
    public List<Venta> getVentas()                       { return ventas; }
    public List<Prestamo> getPrestamos()                 { return prestamos; }
    public List<Reserva> getReservas()                   { return reservas; }

    public void registrarUsuario(Usuario u)              { usuarios.put(u.getLogin(), u); }
    public void agregarJuego(Juego j)                    { juegos.put(j.getNombre(), j); }
    public void agregarCopia(Copia c)                    { copias.put(c.getId(), c); }
    public void agregarProductoMenu(ProductoCafeteria p) { productosMenu.put(p.getNombre(), p); }
    public void crearTorneo(Torneo t)                    { torneos.add(t); }

    public void inscribirEnTorneo(UsuarioComprador usuario, Torneo torneo, int cupos) throws Exception {
        if (cupos > 3 || cupos < 1)
            throw new Exception("Solo se pueden inscribir entre 1 y 3 participantes por usuario.");
        if (usuario instanceof Empleado) {
            Empleado emp = (Empleado) usuario;
            if (emp.getTurno() != null && emp.getTurno().getDia().equalsIgnoreCase(torneo.getDiaSemana()))
                throw new Exception("Los empleados no pueden inscribirse a torneos si tienen turno el mismo dia.");
        }
        boolean esFanatico = usuario.getJuegosFavoritos().contains(torneo.getJuego());
        if (esFanatico && torneo.getCuposFanaticosOcupados() + cupos > torneo.getCuposFanaticosTotal())
            esFanatico = false;
        torneo.inscribir(new Inscripcion(usuario, cupos, esFanatico));
    }

    public void desinscribirDeTorneo(UsuarioComprador usuario, Torneo torneo) {
        torneo.desinscribir(usuario);
    }

    public void realizarPrestamo(UsuarioComprador usuario, Mesa mesa, List<Copia> copiasPedidas, Mesero meseroAcompaniante) throws Exception {
        if (usuario instanceof Empleado && ((Empleado) usuario).estaEnTurno())
            throw new Exception("Un empleado en turno no puede solicitar prestamos.");
        if (usuario.getPrestamosActuales().size() + copiasPedidas.size() > 2)
            throw new Exception("ERROR: Un cliente no puede tener mas de 2 juegos prestados a la vez.");
        boolean tieneJuegoAccion = false;
        for (Copia c : copiasPedidas) {
            Juego j = c.getJuego();
            if (j.getCategoria().equals("Accion")) tieneJuegoAccion = true;
            if (!c.isDisponible() || !c.getInventario().equals("Prestamo"))
                throw new Exception("ERROR: La copia " + c.getId() + " no esta disponible para prestamo.");
            if (mesa != null) {
                if (mesa.getNumPersonas() < j.getMinJugadores() || mesa.getNumPersonas() > j.getMaxJugadores())
                    throw new Exception("ERROR: Restriccion de numero de jugadores para " + j.getNombre());
                if (j.getMinEdad() >= 18 && (mesa.isHayMenores18() || mesa.isHayMenores5()))
                    throw new Exception("ERROR: Juego exclusivo para adultos. Hay menores en la mesa.");
                if (j.getMinEdad() > 5 && mesa.isHayMenores5())
                    throw new Exception("ERROR: Juego no apto para menores de 5 anos.");
            }
            if (j.isEsDificil()) {
                if (meseroAcompaniante == null)
                    System.out.println("ADVERTENCIA: Juego dificil sin mesero introductor.");
                else if (!meseroAcompaniante.getJuegosQueConoce().contains(j))
                    System.out.println("ADVERTENCIA: El mesero no conoce el juego.");
            }
        }
        if (tieneJuegoAccion && mesa != null)
            throw new Exception("No se puede prestar juego de accion con bebidas calientes en la mesa.");
        Prestamo p = new Prestamo(copiasPedidas, usuario, mesa, new java.util.Date().toString(), meseroAcompaniante);
        for (Copia c : copiasPedidas) {
            c.setDisponible(false);
            usuario.agregarPrestamo(c);
        }
        prestamos.add(p);
    }

    public void venderProductos(UsuarioComprador comprador, List<ItemVenta> items, Mesa mesaAtendida) throws Exception {
        double subtotal = 0, impuestos = 0, subCafe = 0, subJuegos = 0;
        for (ItemVenta item : items) {
            Object obj = item.getItem();
            if (obj instanceof Copia) {
                Copia c = (Copia) obj;
                if (!c.getInventario().equals("Venta") || !c.isDisponible())
                    throw new Exception("Copia " + c.getId() + " no disponible para venta.");
                c.setDisponible(false);
                subtotal += item.getSubtotal();
                impuestos += item.getSubtotal() * 0.19;
                subJuegos += item.getSubtotal();
            } else if (obj instanceof ProductoCafeteria) {
                ProductoCafeteria p = (ProductoCafeteria) obj;
                if (p instanceof Bebida) {
                    Bebida b = (Bebida) p;
                    if (b.isEsAlcoholica() && mesaAtendida != null && (mesaAtendida.isHayMenores18() || mesaAtendida.isHayMenores5()))
                        throw new Exception("ERROR: No se puede vender alcohol a mesas con menores.");
                    if (b.isEsCaliente() && tieneMesaJuegoAccion(comprador))
                        throw new Exception("ERROR: No se pueden despachar bebidas calientes si hay juego de Accion prestado.");
                } else if (p instanceof Pasteleria) {
                    Pasteleria pas = (Pasteleria) p;
                    if (!pas.getAlergenos().isEmpty())
                        System.out.println("ATENCION ALERGENOS: " + String.join(", ", pas.getAlergenos()));
                }
                subtotal += item.getSubtotal();
                impuestos += item.getSubtotal() * 0.08;
                subCafe += item.getSubtotal();
            }
        }
        double descuento = 0;
        if (comprador instanceof Empleado) {
            descuento = subtotal * 0.20;
        } else if (comprador instanceof Cliente) {
            Cliente cl = (Cliente) comprador;
            descuento = cl.getPuntosFidelidad();
            cl.restarPuntosFidelidad((int) descuento);
        }
        double propina = subtotal * 0.10;
        int puntos = (int) ((subtotal + impuestos + propina - descuento) * 0.01);
        if (comprador instanceof Cliente) ((Cliente) comprador).agregarPuntosFidelidad(puntos);
        Venta v = new Venta(items, comprador, impuestos, propina, subtotal, descuento, puntos,
                new java.util.Date().toString(), subCafe, subJuegos);
        comprador.registrarCompra(v);
        ventas.add(v);
    }

    private boolean tieneMesaJuegoAccion(UsuarioComprador u) {
        for (Copia c : u.getPrestamosActuales())
            if (c.getJuego().getCategoria().equals("Accion")) return true;
        return false;
    }

    public void cambiarTurno(SolicitudCambioTurno solicitud, Administrador admin) throws Exception {
        if (!cumpleMinimoPersonal())
            throw new Exception("No se puede aprobar el cambio de turno. No se cumple el minimo de empleados.");
        solicitud.aprobar();
        if (solicitud.getReemplazo() != null) {
            Turno temp = solicitud.getSolicitante().getTurno();
            solicitud.getSolicitante().setTurno(solicitud.getReemplazo().getTurno());
            solicitud.getReemplazo().setTurno(temp);
        }
    }

    private boolean cumpleMinimoPersonal() {
        int cocineros = 0, meseros = 0;
        for (Usuario u : usuarios.values()) {
            if (u instanceof Cocinero) cocineros++;
            if (u instanceof Mesero) meseros++;
        }
        return cocineros >= 1 && meseros >= 2;
    }

    // --------------------------------------------------------
    // MÉTODOS PARA GRÁFICAS — DATOS REALES
    // --------------------------------------------------------

    public Map<String, Integer> getDistribucionCopias(String nombreJuego) {
        Map<String, Integer> dist = new HashMap<>();
        dist.put("Prestamo", 0);
        dist.put("Inventario", 0);
        for (Copia c : copias.values()) {
            if (c.getJuego().getNombre().equals(nombreJuego)) {
                if (c.getInventario().equals("Prestamo"))
                    dist.put("Prestamo", dist.get("Prestamo") + 1);
                else if (c.getInventario().equals("Venta"))
                    dist.put("Inventario", dist.get("Inventario") + 1);
            }
        }
        return dist;
    }

    /**
     * Retorna ventas reales del sistema discriminadas por tipo (Cafeteria / Juegos)
     * para una fecha dada. Usa los subtotales almacenados en cada Venta.
     */
    public Map<String, Double> getVentasPorFechaYTipo(String fecha) {
        Map<String, Double> resultado = new HashMap<>();
        resultado.put("Cafeteria", 0.0);
        resultado.put("Juegos", 0.0);
        for (Venta v : ventas) {
            if (v.getFecha().contains(fecha)) {
                resultado.put("Cafeteria", resultado.get("Cafeteria") + v.getSubtotalCafeteria());
                resultado.put("Juegos",    resultado.get("Juegos")    + v.getSubtotalJuegos());
            }
        }
        return resultado;
    }

    public Map<String, Integer> getReservasPorSemana(String inicioSemana, String finSemana) {
        Map<String, Integer> res = new LinkedHashMap<>();
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (String d : dias) res.put(d, 0);

        for (Reserva r : reservas) {
            // Filtrar por rango de fechas si se especifica
            if (inicioSemana != null && finSemana != null) {
                if (!fechaEnRango(r.getFecha(), inicioSemana, finSemana)) continue;
            }
            String d = r.getDiaSemana();
            if (d.equalsIgnoreCase("Miercoles")) d = "Miércoles";
            if (d.equalsIgnoreCase("Sabado"))    d = "Sábado";
            for (String key : res.keySet()) {
                if (key.equalsIgnoreCase(d)) {
                    res.put(key, res.get(key) + 1);
                    break;
                }
            }
        }
        return res;
    }

    private boolean fechaEnRango(String fecha, String inicio, String fin) {
        // Formato dd/MM/yyyy — convertir a yyyyMMdd para comparar como String
        try {
            String f = fecha.substring(6) + fecha.substring(3,5) + fecha.substring(0,2);
            String i = inicio.substring(6) + inicio.substring(3,5) + inicio.substring(0,2);
            String fi = fin.substring(6) + fin.substring(3,5) + fin.substring(0,2);
            return f.compareTo(i) >= 0 && f.compareTo(fi) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    // --------------------------------------------------------
    // PERSISTENCIA DE TORNEOS EN JSON
    // --------------------------------------------------------

    public void guardarTorneosJSON() {
        try {
            File dir = new File("datos");
            if (!dir.exists()) dir.mkdirs();
            JSONObject root = new JSONObject();
            JSONArray arrTorneos = new JSONArray();
            for (Torneo t : torneos) {
                JSONObject obj = new JSONObject();
                obj.put("nombre", t.getNombre());
                obj.put("juego", t.getJuego().getNombre());
                obj.put("diaSemana", t.getDiaSemana());
                obj.put("maxParticipantes", t.getMaxParticipantes());
                if (t instanceof TorneoAmistoso) {
                    obj.put("tipo", "Amistoso");
                } else if (t instanceof TorneoCompetitivo) {
                    obj.put("tipo", "Competitivo");
                    obj.put("tarifaEntrada", ((TorneoCompetitivo) t).getTarifaEntrada());
                }
                JSONArray arrIns = new JSONArray();
                for (Inscripcion ins : t.getInscripciones()) {
                    JSONObject o = new JSONObject();
                    o.put("usuario", ins.getUsuario().getLogin());
                    o.put("cantidadCupos", ins.getCantidadCupos());
                    o.put("esFanatico", ins.isFanatico());
                    arrIns.put(o);
                }
                obj.put("inscripciones", arrIns);
                arrTorneos.put(obj);
            }
            root.put("torneos", arrTorneos);
            JSONArray arrU = new JSONArray();
            for (Usuario u : usuarios.values()) {
                if (u instanceof UsuarioComprador) {
                    UsuarioComprador uc = (UsuarioComprador) u;
                    JSONObject o = new JSONObject();
                    o.put("login", uc.getLogin());
                    JSONArray fav = new JSONArray();
                    for (Juego j : uc.getJuegosFavoritos()) fav.put(j.getNombre());
                    o.put("juegosFavoritos", fav);
                    JSONArray bonos = new JSONArray();
                    for (Double b : uc.getBonosDescuento()) bonos.put(b);
                    o.put("bonosDescuento", bonos);
                    arrU.put(o);
                }
            }
            root.put("usuariosExtra", arrU);
            try (PrintWriter pw = new PrintWriter(new File(dir, "estado_nuevo.json"))) {
                pw.write(root.toString(4));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void cargarTorneosJSON() {
        try {
            File f = new File("datos/estado_nuevo.json");
            if (!f.exists()) return;
            String content = new String(java.nio.file.Files.readAllBytes(f.toPath()));
            JSONObject root = new JSONObject(content);
            if (root.has("usuariosExtra")) {
                JSONArray arr = root.getJSONArray("usuariosExtra");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Usuario u = usuarios.get(o.getString("login"));
                    if (u instanceof UsuarioComprador) {
                        UsuarioComprador uc = (UsuarioComprador) u;
                        JSONArray fav = o.getJSONArray("juegosFavoritos");
                        for (int j = 0; j < fav.length(); j++) {
                            Juego jg = juegos.get(fav.getString(j));
                            if (jg != null) uc.agregarJuegoFavorito(jg);
                        }
                        JSONArray bonos = o.getJSONArray("bonosDescuento");
                        for (int j = 0; j < bonos.length(); j++) uc.agregarBonoDescuento(bonos.getDouble(j));
                    }
                }
            }
            if (root.has("torneos")) {
                JSONArray arr = root.getJSONArray("torneos");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    Juego juego = juegos.get(o.getString("juego"));
                    if (juego == null) continue;
                    Torneo t = null;
                    if (o.getString("tipo").equals("Amistoso"))
                        t = new TorneoAmistoso(o.getString("nombre"), juego, o.getString("diaSemana"), o.getInt("maxParticipantes"));
                    else if (o.getString("tipo").equals("Competitivo"))
                        t = new TorneoCompetitivo(o.getString("nombre"), juego, o.getString("diaSemana"), o.getInt("maxParticipantes"), o.getDouble("tarifaEntrada"));
                    if (t != null) {
                        JSONArray ins = o.getJSONArray("inscripciones");
                        for (int j = 0; j < ins.length(); j++) {
                            JSONObject oi = ins.getJSONObject(j);
                            Usuario u = usuarios.get(oi.getString("usuario"));
                            if (u instanceof UsuarioComprador)
                                t.inscribir(new Inscripcion((UsuarioComprador) u, oi.getInt("cantidadCupos"), oi.getBoolean("esFanatico")));
                        }
                        torneos.add(t);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}