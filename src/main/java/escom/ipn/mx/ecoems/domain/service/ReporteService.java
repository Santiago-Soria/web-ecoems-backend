package escom.ipn.mx.ecoems.domain.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import escom.ipn.mx.ecoems.domain.entity.ResultadoExamen;
import escom.ipn.mx.ecoems.domain.entity.Usuario;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ReporteService {

    public ByteArrayInputStream generarHistorialPdf(Usuario usuario, List<ResultadoExamen> resultados) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.DARK_GRAY);
            Paragraph titulo = new Paragraph("Historial Académico - ECOEMS", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // 2. Datos del Alumno
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            document.add(new Paragraph("Alumno: " + usuario.getNombre() + " " + usuario.getApPaterno() + " " + usuario.getApMaterno(), fontSub));
            document.add(new Paragraph("Correo: " + usuario.getCorreo(), fontSub));
            document.add(Chunk.NEWLINE);

            // 3. Tabla de Resultados
            PdfPTable table = new PdfPTable(3); // 3 columnas
            table.setWidthPercentage(100);

            // Encabezados
            addTableHeader(table, "Examen");
            addTableHeader(table, "Fecha");
            addTableHeader(table, "Calificación");

            // Formato de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Llenar datos
            for (ResultadoExamen resultado : resultados) {
                table.addCell(resultado.getExamen().getNombre());
                table.addCell(sdf.format(resultado.getFecha()));
                table.addCell(resultado.getScoreTotal().toString());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }
}