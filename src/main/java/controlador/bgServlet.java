/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Producto;
import modelo.Utilidades;

/**
 *
 * @author David
 */
public class bgServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private ArrayList<Producto> hamburguesas = null;
    private ArrayList<Producto> complementos = null;
    private ArrayList<Producto> bebidas = null;
    private ArrayList<Producto> listadoProductos = null;
    String rutaFicheros;
    
    
    	public void init(ServletConfig config) throws ServletException {
                rutaFicheros =  config.getServletContext().getRealPath("").
                        concat(File.separator).concat("ficheros").concat(File.separator).concat("productos.txt");
                listadoProductos = new ArrayList<Producto>();
                
        
            /*            listadoProductos.add(new Producto(1,"Hamburguesa normal","burger/normal.png","hamburguesas",5.00f,0));
            listadoProductos.add(new Producto(2,"Hamburguesa con queso","burger/queso.png","hamburguesas",5.50f,0));
            listadoProductos.add(new Producto(3,"Hamburguesa doble","burger/doble.png","hamburguesas",7.00f,0));
            listadoProductos.add(new Producto(4,"Patatas fritas","burger/patatas.png","complementos",1.50f,0));
            listadoProductos.add(new Producto(5,"Nuggets","burger/nuggets.png","complementos",3.00f,0));
            listadoProductos.add(new Producto(6,"Aros de cebolla","burger/aros.png","complementos",2.00f,0));
            listadoProductos.add(new Producto(7,"Agua","burger/agua.png","bebidas",1.00f,0));
            listadoProductos.add(new Producto(8,"Coca-Cola","burger/cocacola.png","bebidas",2.00f,0));
            listadoProductos.add(new Producto(9,"Sprite","burger/sprite.png","bebidas",2.00f,0));
            listadoProductos.add(new Producto(10,"Fanta","burger/fanta.png","bebidas",2.00f,0));*/
            try {
                listadoProductos = Utilidades.getProductos(rutaFicheros);
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(bgServlet.class.getName()).log(Level.SEVERE, null, ex);
                }catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(bgServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
     
                              
               hamburguesas = new ArrayList<Producto>();
               complementos = new ArrayList<Producto>();
               bebidas = new ArrayList<Producto>();
               for ( int i=0;i<listadoProductos.size();i++) {
                   Producto miProducto = listadoProductos.get(i);
                   String categoria = miProducto.getCategoria();
                   if("hamburguesas".equals(categoria)){
                       hamburguesas.add(miProducto);
                   }else if("complementos".equals(categoria)){                       
                       complementos.add(miProducto);
                   }else if ("bebidas".equals(categoria)){
                       bebidas.add(miProducto); 
                   }
                       
                } /*for*/
	}

    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("hamburguesas", hamburguesas);
        request.setAttribute("bebidas", bebidas);
        request.setAttribute("complementos", complementos);
        request.getRequestDispatcher("tpv.jsp").forward(request, response);
    }
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          ArrayList carrito = new ArrayList<Producto>();
       Float total=0f;
       int numeroProductos = listadoProductos.size();
       int ultimoID = listadoProductos.get(numeroProductos-1).getId();
       for (int i=1;i<=ultimoID;i++) {
           int unidades=0;
           if ( request.getParameter(String.valueOf(i))!=null  ){
               if ( !request.getParameter(String.valueOf(i)).equals("")) {
                 unidades = Integer.parseInt(request.getParameter(String.valueOf(i)));
               }                
            }
           if ( unidades>0 ) {
               for ( Producto prod:listadoProductos ) {
                   if ( prod.getId() == i  ) {
                       total = total + unidades * prod.getPrecio();
                       Producto productoAlCarrito = new Producto(prod.getId(),prod.getNombre(),
                               prod.getImagen(),prod.getCategoria(), prod.getPrecio(), unidades );
                       carrito.add(productoAlCarrito);
                   }
               
               }
             }
         }//recorre los inputs del formulario tpv
       request.setAttribute("carrito", carrito);
       request.setAttribute("total", total);
       request.getRequestDispatcher("resumen.jsp").forward(request, response);
           }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
