package scr.util;

import scr.events.AcademicEvents;
import scr.participants.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CertificateGenerator {

    public void generateCertificate(AcademicEvents event, Participant participant) {
        String baseFileName = "certificate_"
                + participant.getName().replaceAll("[^a-zA-Z0-9.-]", "_")
                + "_" + event.getTitle().replaceAll("[^a-zA-Z0-9.-]", "_");
        String fileName = "certificates/" + baseFileName + ".html";

        new File("certificates").mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            String htmlContent = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
			
                    <title>Certificado de Participação</title>
                    <style>
                        body { font-family: 'Arial', sans-serif; margin: 20px; text-align: center; background-color: #f0f0f0; }
                        .certificate-container { background-color: #fff; border: 2px solid #333; padding: 40px; margin: 20px auto; max-width: 800px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                        h1 { color: #0056b3; margin-bottom: 20px; }
                        .participant-name { font-size: 2em; font-weight: bold; margin: 30px 0; color: #007bff; }
                        .event-details { font-size: 1.2em; margin-bottom: 30px; }
                        .signature { margin-top: 50px; border-top: 1px solid #333; padding-top: 10px; display: inline-block; }
                        .date-issued { margin-top: 20px; font-size: 0.9em; color: #777; }
                    </style>
                </head>
                <body>
                    <div class="certificate-container">
                        <h1>CERTIFICATE OF PARTICIPATION</h1>
                        <p>We certify that:</p>
                        <p class="participant-name">%s</p>
                        <p>participated in the event:</p>
                        <p class="event-details"><strong>%s</strong></p>
                        <p>carried out in %s %s.</p>
                        %s
                        <div class="signature">
                            <p>_______________________________________</p>
                            <p>Organization Signature</p>
                        </div>
                        <p class="date-issued">Issue Date: %s</p>
                    </div>
                </body>
                </html>
                """.formatted(
                    participant.getName().toUpperCase(),
                    event.getTitle(),
                    event.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    event.getLocation(),
                    (event.getDescription() != null && !event.getDescription().isEmpty() ?
                            "<p>Event Description: " + event.getDescription() + "</p>" : ""),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            writer.print(htmlContent);

            System.out.println("HTML certificate generated successfully: " + fileName);

        } catch (IOException e) {
            System.err.println("Error generating HTML certificate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}