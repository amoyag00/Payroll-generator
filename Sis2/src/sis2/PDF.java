/*
 * Class which generates PDF file with payrollTable of an employee.
 */
package sis2;

import com.itextpdf.text.BadElementException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;

/**
 *
 * @author Alejandro Moya García
 * @version 4.0 02/06/2018
 */
public class PDF {

    private Document document;
    private PdfWriter writer;
    private Employee emp;
    private int month;
    private int year;
    private final String PDF_PATH = System.getProperty("user.dir")
            + "/resources/prueba.pdf";
    private final int PRECISSION = 2;
    private PdfPTable payrollTable;
    private Payroll payroll;
    private boolean isExtra;

    public PDF(Employee emp, int month, int year, boolean isExtra)
            throws FileNotFoundException, DocumentException {
        this.emp = emp;
        this.month = month;
        this.year = year;
        if (isExtra) {
            this.payroll = emp.getExtraPayroll();
        } else {
            this.payroll = emp.getPayroll();
        }
        this.isExtra = isExtra;
    }

    /**
     * Prints the pdf.
     *
     * @param filename
     * @throws DocumentException
     * @throws BadElementException
     * @throws IOException
     */
    public void print(String filename) throws DocumentException,
            BadElementException, IOException {
        this.document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        Paragraph empty = new Paragraph("");
        PdfPTable tableDatosEmpresa = new PdfPTable(2);

        Paragraph nom = new Paragraph(this.emp.getCompanyName());
        Paragraph cif = new Paragraph(this.emp.getCompanyCIF());
        Paragraph dir1 = new Paragraph("Avenida de la Facultad");
        Paragraph dir2 = new Paragraph("24001 León");

        PdfPCell cell1 = new PdfPCell();
        cell1.addElement(nom);
        cell1.addElement(cif);
        cell1.addElement(empty);
        cell1.addElement(dir1);
        cell1.addElement(dir2);
        cell1.addElement(empty);

        cell1.setPadding(1);
        PdfPCell cell2 = new PdfPCell(new Paragraph());
        cell2.setBorder(Rectangle.NO_BORDER);

        Paragraph iban = new Paragraph("IBAN: " + this.emp.getIBAN());
        Paragraph cat = new Paragraph("Categoria: " + this.emp.getCategory().
                getCategoryName());
        Paragraph yearlyGross = new Paragraph("Bruto Anual: "
                + round(this.payroll.getYearlyGross(), 2));
        Paragraph entryDate = new Paragraph("Fecha de alta: "
                + this.emp.getCompanyEntryLocalDate());
        cell2.addElement(iban);
        cell2.addElement(cat);
        cell2.addElement(yearlyGross);
        cell2.addElement(entryDate);
        tableDatosEmpresa.addCell(cell1);
        tableDatosEmpresa.addCell(cell2);
        tableDatosEmpresa.setSpacingAfter(10f);
        document.add(tableDatosEmpresa);

        PdfPTable tableDatosTrabajador = new PdfPTable(2);

        Paragraph destinatario = new Paragraph("Destinatario: ");
        Paragraph nomTrabajador = new Paragraph(emp.getName() + " "
                + emp.getSurname1() + " " + emp.getSurname2());
        nomTrabajador.setAlignment(Element.ALIGN_RIGHT);
        Paragraph niftrab = new Paragraph("DNI: " + emp.getID().toString());
        niftrab.setAlignment(Element.ALIGN_RIGHT);
        Paragraph dir1trab = new Paragraph("Avenida de la facultad");
        dir1trab.setAlignment(Element.ALIGN_RIGHT);
        Paragraph dir2trab = new Paragraph("24001 León");
        dir2trab.setAlignment(Element.ALIGN_RIGHT);

        Image img = Image.getInstance("resources/img.jpg");
        PdfPCell cellImagen = new PdfPCell(img, true);
        cellImagen.setFixedHeight(100f);
        cellImagen.setBorder(Rectangle.NO_BORDER);
        cellImagen.setPadding(10);
        cellImagen.setPaddingTop(5);

        PdfPCell celltrabajador = new PdfPCell();
        celltrabajador.addElement(destinatario);
        celltrabajador.addElement(nomTrabajador);
        celltrabajador.addElement(niftrab);
        celltrabajador.addElement(empty);
        celltrabajador.addElement(dir1trab);
        celltrabajador.addElement(dir2trab);
        celltrabajador.addElement(empty);

        celltrabajador.setIndent(10);
        celltrabajador.setPadding(10);

        tableDatosTrabajador.addCell(cellImagen);
        tableDatosTrabajador.addCell(celltrabajador);
        tableDatosTrabajador.setSpacingAfter(10f);
        document.add(tableDatosTrabajador);
        ///

        ///
        PdfPTable tablaFecha = new PdfPTable(1);

        Font fontTit = new Font(FontFamily.HELVETICA, 12,
                Font.BOLDITALIC, GrayColor.BLACK);
        Font fontGray = new Font(FontFamily.HELVETICA, 12,
                Font.NORMAL, GrayColor.LIGHT_GRAY);
        String text = "";
        if (isExtra) {
            text = "Extra: ";
        } else {
            text = "Nomina: ";
        }
        text += getMonthName(month) + " de " + year;
        Paragraph fechLit = new Paragraph(text, fontTit);
        fechLit.setAlignment(Element.ALIGN_CENTER);
        PdfPCell cellfecha2 = new PdfPCell();
        cellfecha2.addElement(fechLit);
        cellfecha2.setBorder(Rectangle.NO_BORDER);
        cellfecha2.setPadding(10);
        tablaFecha.addCell(cellfecha2);

        PdfPCell cellfecha3 = new PdfPCell(new Paragraph());
        cellfecha2.setBorder(Rectangle.NO_BORDER);
        tablaFecha.addCell(cellfecha3);

        tablaFecha.setSpacingAfter(10f);
        document.add(tablaFecha);

        //Payroll
        this.payrollTable = new PdfPTable(6);

        this.addToPayroll("", 1, false);
        this.addToPayroll("", 1, false);
        this.addToPayroll("cant.", 1, false);
        this.addToPayroll("Imp. Unit", 1, false);
        this.addToPayroll("Dev.", 1, false);
        this.addToPayroll("Deducc.", 1, false);

        printLine(false);
        payrollTable.setSpacingAfter(10f);

        this.addToPayroll("Salario base", 2, false);
        this.addToPayroll("30 dias", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMonthlySalary() / 30.0, PRECISSION)), 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMonthlySalary(), PRECISSION)), 1, false);
        this.addToPayroll("", 1, false);

        this.addToPayroll("Prorrata", 2, false);
        this.addToPayroll("30 días", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getProRateValue() / 30.0, 2)), 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getProRateValue(), 2)), 1, false);
        this.addToPayroll("", 1, false);

        this.addToPayroll("Complemento", 2, false);
        this.addToPayroll("30 días", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMonthlyComplement() / 30.0, 2)), 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMonthlyComplement(), 2)), 1, false);
        this.addToPayroll("", 1, false);

        this.addToPayroll("Antigüedad", 2, false);
        this.addToPayroll(String.valueOf(this.payroll.getNumThreeYears())+
                " trienios", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMoneyThreeYears() / 30.0, 2)), 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMoneyThreeYears(), 2)), 1, false);
        this.addToPayroll("", 1, false);

        payrollTable.setSpacingAfter(10f);
        breakLine();

        this.addToPayroll("Contingencias generales", 2, false);
        this.addToPayroll(String.valueOf(
                this.payroll.getGeneralContingenciesEmp()) + "%", 1, false);
        this.addToPayroll("de " + String.valueOf(round(
                this.payroll.getRegulatoryBase(), 2)), 1, false);
        this.addToPayroll("", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getGeneralContingenciesMoneyEmp(), 2)), 1, false);

        this.addToPayroll("Desempleo", 2, false);
        this.addToPayroll(String.valueOf(
                this.payroll.getDisempEmp()) + "%", 1, false);
        this.addToPayroll("de " + String.valueOf(round(
                this.payroll.getRegulatoryBase(), 2)), 1, false);
        this.addToPayroll("", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getDisempMoneyEmp(), 2)), 1, false);

        this.addToPayroll("Cuota Formación", 2, false);
        this.addToPayroll(String.valueOf(
                this.payroll.getFormationEmp()) + "%", 1, false);
        this.addToPayroll("de " + String.valueOf(round(
                this.payroll.getRegulatoryBase(), 2)), 1, false);
        this.addToPayroll("", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getFormationMoneyEmp(), 2)), 1, false);

        this.addToPayroll("IRPF", 2, false);
        this.addToPayroll(String.valueOf(
                this.payroll.getIrpf()) + "%", 1, false);
        this.addToPayroll("de " + String.valueOf(round(
                this.payroll.getMonthlyGross(), 2)), 1, false);
        this.addToPayroll("", 1, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getIrpfMoney(), 2)), 1, false);

        printLine(false);

        double totalDeduccAux = this.payroll.getIrpfMoney()
                + this.payroll.getGeneralContingenciesMoneyEmp()
                + this.payroll.getDisempMoneyEmp()
                + this.payroll.getFormationMoneyEmp();
        this.addToPayroll("Total Deducciones", 2, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll(String.valueOf(
                round(totalDeduccAux, 2)), 1, false);

        this.addToPayroll("Total Devengos", 2, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll(String.valueOf(
                round(this.payroll.getMonthlyGross(), 2)), 1, false);
        this.addToPayroll(" ", 1, false);

        printLine(false);

        this.addToPayroll(" ", 1, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll(" ", 1, false);
        this.addToPayroll("Líquido a percibir", 2, false);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getMonthlyGross() - totalDeduccAux, 2)), 1, false);

        breakLine();
        this.addToPayroll("Cálculo Empresario BASE:", 5, true);
        this.addToPayroll(String.valueOf(
                round(this.payroll.getRegulatoryBase(), 2)), 1, true);

        breakLine();
        printLine(true);

        this.addToPayroll("Contingencias comunes", 2, true);
        this.addToPayroll(String.valueOf(
                this.payroll.getCommonContingenciesEntr() + "%"), 3, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getCommonContingenciesMoneyEntr(), 2)), 1, true);

        this.addToPayroll("Desempleo", 1, true);
        this.addToPayroll(String.valueOf(
                this.payroll.getDisempEntr() + "%"), 4, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getDisempMoneyEntr(), 2)), 1, true);

        this.addToPayroll("Formación", 1, true);
        this.addToPayroll(String.valueOf(
                this.payroll.getFormationEntr() + "%"), 4, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getFormationMoneyEntr(), 2)), 1, true);

        this.addToPayroll("Accidentes de trabajo", 2, true);
        this.addToPayroll(String.valueOf(
                this.payroll.getAccidentsEntr() + "%"), 3, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getAccidentsMoneyEntr(), 2)), 1, true);

        this.addToPayroll("FOGASA", 1, true);
        this.addToPayroll(String.valueOf(
                this.payroll.getFogasaEntr() + "%"), 4, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getFogasaMoneyEntr(), 2)), 1, true);

        printLine(true);
        breakLine();

        this.addToPayroll("Total Empresario", 5, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getFogasaMoneyEntr()
                + this.payroll.getAccidentsMoneyEntr()
                + this.payroll.getFormationMoneyEntr()
                + this.payroll.getDisempMoneyEntr()
                + this.payroll.getCommonContingenciesMoneyEntr(), 2)), 1, true);

        this.addToPayroll("Coste total trabajador", 5, true);
        this.addToPayroll(String.valueOf(round(
                this.payroll.getFogasaMoneyEntr()
                + this.payroll.getAccidentsMoneyEntr()
                + this.payroll.getFormationMoneyEntr()
                + this.payroll.getDisempMoneyEntr()
                + this.payroll.getCommonContingenciesMoneyEntr()
                + this.payroll.getMonthlyGross(), 2)), 1, true);
        breakLine();

        document.add(payrollTable);
        this.document.close();
    }

    /**
     * Returns the string name of the month asscoiated to a number.
     *
     * @param month
     * @return the string name of the month
     */
    public String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";

        }
        return "";
    }

    /**
     * rounds a decimal number the specified places.
     *
     * @param value
     * @param places
     * @return the rounded number.
     */
    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Adds a cell wich spans colspan and the specified color to the payroll
     * table.
     *
     * @param value
     * @param colspan
     * @param gray
     */
    public void addToPayroll(String value, int colspan, boolean gray) {
        PdfPCell cell = new PdfPCell(new Paragraph(value));
        Font fontGray = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
                GrayColor.LIGHT_GRAY);
        if (gray) {
            cell = new PdfPCell(new Paragraph(value, fontGray));
        }
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(colspan);
        this.payrollTable.addCell(cell);
    }

    /**
     * Writes a space line in the PDF.
     */
    public void breakLine() {
        for (int i = 0; i < 6; i++) {
            PdfPCell cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            payrollTable.addCell(cell);
        }
    }

    /**
     * Writes a separation line in the PDF. gray decides if color is black or
     * gray.
     *
     * @param gray
     */
    public void printLine(boolean gray) {
        for (int i = 0; i < 6; i++) {
            PdfPCell cell = new PdfPCell(new Paragraph(""));
            Font fontGray = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
                    GrayColor.LIGHT_GRAY);
            if (gray) {
                cell.setBorderColor(GrayColor.LIGHT_GRAY);
            }

            this.payrollTable.addCell(cell);
        }

    }

}
