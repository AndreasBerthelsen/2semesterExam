package dk.easv.bll.Util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class GenInfoTabFactory {
    private static Queue<String> infoStrings = getInfoStrings();

    public GenInfoTabFactory() {
        infoStrings = getInfoStrings();
    }

    public static ScrollPane createGenInfoContent(List<String> fieldList, Map<String,TextArea> answerMap) {
        VBox contentBox = new VBox(60);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentBox.setPadding(new Insets(40, 80, 80, 80));
        contentBox.setId("VBOX");
        contentBox.getStylesheets().add("dk/easv/CSS/Skabelon.css");
        for (String fieldName : fieldList) {
            createChunk(fieldName,answerMap,contentBox,null);
        }
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private static VBox createChunk(String fieldName, Map<String,TextArea> answerMap,VBox contentBox,String startingText){
        VBox chunk = new VBox();
        chunk.setAlignment(Pos.TOP_CENTER);
        TextArea textArea = createTextArea(startingText);
        chunk.getChildren().addAll(createHeaderHBox(fieldName), textArea);
        contentBox.getChildren().addAll(chunk);
        answerMap.put(fieldName,textArea);
        return chunk;
    }

    private static HBox createHeaderHBox(String headerString) {

        Label headerLabel = new Label(headerString.replaceAll("_"," "));
        ImageView infoImage = createInfoImage();
        HBox headerBox = new HBox(5, headerLabel, infoImage);
        headerBox.setAlignment(Pos.CENTER);
        return headerBox;
    }

    private static ImageView createInfoImage() {
        int imageSize = 15;
        int hoverDelayMills = 200;

        ImageView imageView = new ImageView(new Image("/dk/easv/Images/info.png"));
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        imageView.setSmooth(true);

        if (!infoStrings.isEmpty()){
            String s = infoStrings.poll();
            Tooltip tooltip = new Tooltip(s);

            tooltip.setShowDelay(new Duration(hoverDelayMills));
            tooltip.setHideDelay(Duration.INDEFINITE);
            imageView.setOnMouseClicked(event -> tooltip.hide());

            Tooltip.install(imageView, tooltip);
        }

        return imageView;
    }

    private static TextArea createTextArea(String startingText) {
        int width = 600;
        TextArea textArea = new TextArea(startingText);
        textArea.setWrapText(true);
        textArea.setMaxWidth(width);
        return textArea;
    }

    private static Queue<String> getInfoStrings() {
        Queue<String> q = new LinkedBlockingQueue<>();
        q.add("""
                Borgerens bevidste eller ubevidste
                håndtering af livet/sygdommen – både
                udfordringer og muligheder""");
        q.add("""
                Drivkraften bag at borgeren handler på
                en bestemt måde eller går i gang
                med/opretholder en opgave/indsats.
                """);
        q.add("""
                De fysiske eller mentale kræfter, som
                borgerenen i et vist omfang har til
                rådighed og kan udnytte. Fysiske kræfter
                kan fx være i form af fysisk sundhed og
                styrke. Mentale kræfter kan fx være i
                form af psykisk sundhed og styrke,
                herunder tanker og måder at forholde sig
                til situationer og andre mennesker på.""");
        q.add("""
                De roller som er særligt vigtige for
                borgeren i forhold til familie, arbejde og
                samfund.""");
        q.add("""
                Regelmæssig adfærd som borgeren har
                tillært gennem stadig gentagelse og
                udførelse helt eller delvist ubevidst.
                Vaner er fx døgnrytmen, måden at blive
                tiltalt på, kontakt med medmennesker og
                relationer, måde at anskue verden på.
                """);
        q.add("""
                Nuværende eller tidligere uddannelsesog/eller erhvervsmæssig baggrund.
                Fx folkeskole, erhvervsuddannelse og
                videregående uddannelse.
                """);
        q.add("""
                En beskrivelse af borgerens oplevelse af
                væsentlige begivenheder, interesser og
                gøremål igennem livet""");
        q.add("""
                Personer som er tæt på borgeren, og
                som giver praktisk og/eller
                følelsesmæssigt støtte og omsorg
                overfor borgeren. Netværk kan være
                offentligt eller privat.
                Et offentligt netværk består af personlige
                hjælpere, sundhedspersonale og andre
                professionelle primært omsorgsgivere.
                Privat netværk er familie, slægtning,
                venner og bekendtskaber.""");
        q.add("""
                Helbredsoplysninger:
                Aktuelle eller tidligere sygdomme og
                handicap der har betydning for
                borgerens situation.
                Sundhedsfaglige kontakter:
                Medarbejder eller enheder indenfor
                sundhedsvæsenet borgeren er tilknyttet,
                fx øjenlæge, tandlæge, fodterapeut eller
                afdeling/ambulatorium.
                """);
        q.add("""
                Udstyr, produkter og teknologi som
                anvendes af borgeren i daglige
                aktiviteter, inkl. sådanne som er tilpasset
                eller særligt fremstillet til, implanteret i,
                placeret på eller nær personen, som
                anvender dem. (Inkl. almindelige
                genstande og hjælpemidler og teknologi
                til personlig anvendelse).\s""");
        q.add("""
                En beskrivelse af boligens fysiske rammer
                og omgivelser, der har betydning for
                borgerens hverdagsliv og funktionsevne.""");
        return q;
    }

    public static ScrollPane createGenInfoContentWithInfo(List<String> fieldList, Map<String,TextArea> answerMap,Map<String,String> infoMap){
        VBox contentBox = new VBox(60);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentBox.setPadding(new Insets(40, 80, 80, 80));
        contentBox.setId("VBOX");
        contentBox.getStylesheets().add("dk/easv/CSS/Skabelon.css");
        for (String fieldName : fieldList) {
            createChunk(fieldName,answerMap,contentBox,infoMap.get(fieldName));
        }
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
}
