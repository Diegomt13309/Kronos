/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicWeb;

import logic.Accord;
import data.Dao;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.TempUser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jonathan
 */
@WebServlet(name = "AccordServelet", urlPatterns = {"/AccordServelet", "/accord/addAccord"})
public class AccordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String UPLOAD_DIRECTORY = "/home/jonathan/uploads";
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 10;  // 10MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException, Exception {
        String a = request.getServletPath();
        if (request.getServletPath().equals("/accord/addAccord")) {
            this.addAccord(request, response);
        }

    }
// 

    private void addAccord(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException, Exception {
        if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            throw new Exception();
        }
        try {

            
            Accord acc = new Accord();

            // configures upload settings
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // sets memory threshold - beyond which files are stored in disk
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // sets temporary location to store files
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);

            // sets maximum size of upload file
            upload.setFileSizeMax(MAX_FILE_SIZE);

            // sets maximum size of request (include file + form data)
            upload.setSizeMax(MAX_REQUEST_SIZE);

            // constructs the directory path to store upload file
            // this path is relative to application's directory
            String uploadPath = UPLOAD_DIRECTORY;

            // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            TempUser tmp = new TempUser();
            // parses the request's content to extract file data
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date incorDate = new Date();
            acc.setIncorporatedDate(incorDate);
            
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {

                    if (!item.isFormField()) {

                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        acc.getURL().add(filePath);
                        item.write(storeFile);

                    } else {
                        String name = item.getFieldName();
                        switch (name) {
                            case "office":
                                String accNumber = new String(item.get());
                                String newAccNumber = "MSPH-CM-ACUER-" + accNumber;
                                acc.setAccNumber(newAccNumber);
                                break;
                            case "generalSession": {
                                String date = new String(item.get());
                                Date sessDate = format.parse(date);
                                acc.setSessionDate(sessDate);
                                Dao.getDao().isIncorDate(sessDate);

                            }
                            break;

                            case "deadline": {
                                String date = new String(item.get());
                                Date deadline = format.parse(date);
                                acc.setDeadline(deadline);
                            }
                            break;
//
//                            case "incorDate": {
//                                String date = new String(item.get());
//                                Date incorDate = format.parse(date);
//                                acc.setIncorporatedDate(incorDate);
//                            }
//                            break;

                            case "observations": {
                                String observations = new String(item.get());
                                acc.setObservations(observations);
                            }
                            break;

                            case "comboTypes": {
                                String type = new String(item.get());
                                acc.setType(type.charAt(0));
                            }
                            break;
                            case "username": {
                                String tempName = new String(item.get());
                                if (tempName != null) {
                                    if (tempName != "") {

                                        tmp.setName(tempName);

                                    }
                                }
                            }
                            break;

                            case "email": {
                                String tempEmail = new String(item.get());
                                if (tempEmail != null) {
                                    if (tempEmail != "") {

                                        tmp.setEmail(tempEmail);

                                    }
                                }
                            }
                            break;
                        
                            default:
                                break;
                        }
                    }
                }
            }

            acc.setIncorporatedDate(new Date());
            acc.setPublished(false);
            boolean flag=false;
            if (acc.getType() != 'A' && acc.getType() != 'Z') {
                flag=true;
                if (!tmp.getEmail().equals("null")) {
                    TempUser temp = Dao.getDao().getTempUserByEmail(tmp.getEmail());
                    if (temp == null) 
                        Dao.getDao().insertTempUser(tmp);
                }
                acc.setNotified(false);
            }
            else
                acc.setNotified(true);
            
                acc.setState(2);
                Dao.getDao().insertAccord(acc);
                
                if (flag) 
                    Dao.getDao().insertUserAccord(tmp, acc);
                else
                    Dao.getDao().insertAccordNotification(acc.getAccNumber());

            }
            catch (Exception ex) {
                String m=ex.getMessage();
            throw new ServletException("Error al Agregar acuerdo");
        }
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
        protected void doGet
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            try {
                processRequest(request, response);
            } catch (Exception ex) {
                Logger.getLogger(AccordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        protected void doPost
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            try {
                processRequest(request, response);
            } catch (Exception ex) {
                    response.setStatus(503);
                   //throw new ServletException(ex.getMessage());
            }
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo
        
            () {
        return "Short description";
        }// </editor-fold>

    }
