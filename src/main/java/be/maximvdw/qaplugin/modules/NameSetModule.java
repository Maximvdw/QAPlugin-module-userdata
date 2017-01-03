package be.maximvdw.qaplugin.modules;

import be.maximvdw.qaplugin.api.AIModule;
import be.maximvdw.qaplugin.api.AIQuestionEvent;
import be.maximvdw.qaplugin.api.QAPluginAPI;
import be.maximvdw.qaplugin.api.ai.*;
import be.maximvdw.qaplugin.api.exceptions.FeatureNotEnabled;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * NameSetModule
 * Created by maxim on 01-Jan-17.
 */
public class NameSetModule extends AIModule {
    public NameSetModule() {
        super("user.name.set", "Maximvdw", "Allows you to give your name");

        Intent question = new Intent("QAPlugin-module-userdata-user.name.set")
                .addTemplate(new IntentTemplate()
                        .addPart("My first name is ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("My given name is ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("My name is ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("Call me ")
                        .addPart(new IntentTemplate.TemplatePart("John")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("You can call me ")
                        .addPart(new IntentTemplate.TemplatePart("Abraham")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("Call me ")
                        .addPart(new IntentTemplate.TemplatePart("Cloe")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name"))
                        .addPart(" from now on"))
                .addTemplate(new IntentTemplate()
                        .addPart("I am ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("My name is ")
                        .addPart(new IntentTemplate.TemplatePart("Erwin")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name"))
                        .addPart(" ")
                        .addPart(new IntentTemplate.TemplatePart("Vanden Bosche")
                                .withAlias("last-name")
                                .withMeta("@sys.last-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("Call me ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name"))
                        .addPart(" ")
                        .addPart(new IntentTemplate.TemplatePart("Kaspersky")
                                .withAlias("last-name")
                                .withMeta("@sys.last-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("You can call me ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name"))
                        .addPart(" ")
                        .addPart(new IntentTemplate.TemplatePart("Baba luga")
                                .withAlias("last-name")
                                .withMeta("@sys.last-name")))
                .addTemplate(new IntentTemplate()
                        .addPart("Call me ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name"))
                        .addPart(" ")
                        .addPart(new IntentTemplate.TemplatePart("Van de Wynckel")
                                .withAlias("last-name")
                                .withMeta("@sys.last-name"))
                        .addPart(" from now on"))
                .addTemplate(new IntentTemplate()
                        .addPart("I am ")
                        .addPart(new IntentTemplate.TemplatePart("Maxim")
                                .withAlias("given-name")
                                .withMeta("@sys.given-name"))
                        .addPart(" ")
                        .addPart(new IntentTemplate.TemplatePart("Van de Wynckel")
                                .withAlias("last-name")
                                .withMeta("@sys.last-name")))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addParameter(new IntentResponse.ResponseParameter("given-name", "$given-name")
                                .setRequired(true)
                                .withDataType("@sys.given-name")
                                .addPrompt("Didn't catch that name sorry. Say again please?")
                                .addPrompt("What is your name?")
                                .addPrompt("What's your name?")
                                .addPrompt("Say your name please"))
                        .addParameter(new IntentResponse.ResponseParameter("last-name", "$last-name")
                                .withDataType("@sys.last-name"))
                        .addMessage(new IntentResponse.TextResponse()
                                .addSpeechText("Ok, $given-name!")
                                .addSpeechText("I will try and remember that ;)")
                                .addSpeechText("I will remember that $given-name!")
                                .addSpeechText("Hi there $given-name!")));
        try {
            // Upload the intents
            if (QAPluginAPI.findIntentByName(question.getName()) == null) {
                if (!QAPluginAPI.uploadIntent(question)) {
                    warning("Unable to upload intent!");
                }
            }
        } catch (FeatureNotEnabled ex) {
            severe("You do not have a developer access token in your QAPlugin config!");
        }
    }

    public String getResponse(AIQuestionEvent event) {
        Player player = event.getPlayer();
        Map<String,String> params = event.getParameters();
        String givenName = null;
        String lastName = null;
        if (!params.containsKey("given-name")) {
            return event.getDefaultResponse();
        }else{
            givenName = params.get("given-name");
            QAPluginAPI.setPlayerDataValue(player,"user.name.firstname",givenName);
        }

        if (params.containsKey("last-name")){
            lastName = params.get("last-name");
            QAPluginAPI.setPlayerDataValue(player,"user.name.lastname",lastName);
        }

        return event.getDefaultResponse();
    }
}
