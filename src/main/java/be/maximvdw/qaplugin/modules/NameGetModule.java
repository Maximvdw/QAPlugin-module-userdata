package be.maximvdw.qaplugin.modules;

import be.maximvdw.qaplugin.api.AIModule;
import be.maximvdw.qaplugin.api.AIQuestionEvent;
import be.maximvdw.qaplugin.api.QAPluginAPI;
import be.maximvdw.qaplugin.api.ai.*;
import be.maximvdw.qaplugin.api.exceptions.FeatureNotEnabled;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * NameGetModule
 * Created by maxim on 01-Jan-17.
 */
public class NameGetModule extends AIModule {
    public NameGetModule() {
        super("user.name.get", "Maximvdw", "Allows you to get your name");

        Entity getType = new Entity("UserNameType")
                .addEntry(new EntityEntry("NAME")
                        .addSynonym("name"))
                .addEntry(new EntityEntry("FIRST_NAME")
                        .addSynonym("firstname")
                        .addSynonym("first name")
                        .addSynonym("given name"))
                .addEntry(new EntityEntry("LAST_NAME")
                        .addSynonym("lastname")
                        .addSynonym("last name")
                        .addSynonym("surname")
                        .addSynonym("sur name"));

        Intent question = new Intent("QAPlugin-module-userdata-user.name.get")
                .addTemplate(new IntentTemplate()
                        .addPart("what is my ")
                        .addPart(new IntentTemplate.TemplatePart("name")
                                .withMeta(getType)
                                .withAlias("get-type"))
                        .addPart("?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what is my ")
                        .addPart(new IntentTemplate.TemplatePart("name")
                                .withMeta(getType)
                                .withAlias("get-type")))
                .addTemplate(new IntentTemplate()
                        .addPart("do you know my ")
                        .addPart(new IntentTemplate.TemplatePart("first name")
                                .withMeta(getType)
                                .withAlias("get-type"))
                        .addPart("?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what is my ")
                        .addPart(new IntentTemplate.TemplatePart("name")
                                .withMeta(getType)
                                .withAlias("get-type")))
                .addTemplate("who am I?")
                .addTemplate(new IntentTemplate()
                        .addPart("tell me my ")
                        .addPart(new IntentTemplate.TemplatePart("last name")
                                .withMeta(getType)
                                .withAlias("get-type"))
                        .addPart("?"))
                .addTemplate("do you remember me?")
                .addTemplate(new IntentTemplate()
                        .addPart("give me my ")
                        .addPart(new IntentTemplate.TemplatePart("first name")
                                .withMeta(getType)
                                .withAlias("get-type"))
                        .addPart("?"))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addParameter(new IntentResponse.ResponseParameter("get-type", "$get-type")
                                .withDataType(getType)
                                .setRequired(false)
                                .withDefaultValue("NAME"))
                        .addMessage(new IntentResponse.TextResponse()
                                .addSpeechText("You are {{firstname}} {{lastname}}!")
                                .addSpeechText("Your full name is {{firstname}} {{lastname}}")
                                .addSpeechText("Your name is {{firstname}} {{lastname}}")
                                .addSpeechText("{{firstname}} {{lastname}}"))
                        .addMessage(new IntentResponse.TextResponse()
                                .addSpeechText("You are {{firstname}}!")
                                .addSpeechText("Your full name is {{firstname}}")
                                .addSpeechText("Your name is {{firstname}}")
                                .addSpeechText("{{firstname}} is your first name"))
                        .addMessage(new IntentResponse.TextResponse()
                                .addSpeechText("Your last name is {{lastname}}")
                                .addSpeechText("Your surname is {{lastname}}"))
                        .addMessage(new IntentResponse.TextResponse()
                                .addSpeechText("I do not know your name :S")
                                .addSpeechText("You have to tell it to me first")
                                .addSpeechText("You never told me your name")
                                .addSpeechText("Tell it first, I will remember it forever! ;)"))
                        .addMessage(new IntentResponse.TextResponse()
                                .addSpeechText("I do not know your last name")
                                .addSpeechText("Not a clue what your last name is")
                                .addSpeechText("You have to tell it to me first")
                                .addSpeechText("You never told me!")
                                .addSpeechText("I don't know")));
        try {
            // Upload the entities
            if (QAPluginAPI.findEntityByName(getType.getName()) == null) {
                if (!QAPluginAPI.uploadEntity(getType)) {
                    warning("Unable to upload entity!");
                }
            }

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
        Map<String, String> params = event.getParameters();

        NameGetType type = NameGetType.NAME;
        if (params.containsKey("get-type")) {
            try {
                type = NameGetType.valueOf(params.get("get-type"));
            } catch (Exception ex) {

            }
        }

        String answerFullName = ((IntentResponse.TextResponse) event.getDefaultResponses().get(0)).getSpeechTexts().get(0);
        String answerFirstName = ((IntentResponse.TextResponse) event.getDefaultResponses().get(1)).getSpeechTexts().get(0);
        String answerLastName = ((IntentResponse.TextResponse) event.getDefaultResponses().get(2)).getSpeechTexts().get(0);
        String errorUnknownName = ((IntentResponse.TextResponse) event.getDefaultResponses().get(3)).getSpeechTexts().get(0);
        String errorUnknownLastName = ((IntentResponse.TextResponse) event.getDefaultResponses().get(4)).getSpeechTexts().get(0);

        String firstName = QAPluginAPI.getPlayerDataValue(player, "user.name.firstname");
        String lastName = QAPluginAPI.getPlayerDataValue(player, "user.name.lastname");

        switch (type) {
            case NAME:
                if (firstName == null) {
                    return errorUnknownName;
                }
                if (lastName != null) {
                    return answerFullName.replace("{firstname}", firstName).replace("{lastname}", lastName);
                } else {
                    return answerFirstName.replace("{firstname}", firstName);
                }
            case FIRST_NAME:
                if (firstName == null) {
                    return errorUnknownName;
                }
                return answerFirstName.replace("{firstname}", firstName);
            case LAST_NAME:
                if (lastName == null) {
                    return errorUnknownLastName;
                }
                return answerLastName.replace("{lastname}", lastName);
        }

        return null;
    }

    public enum NameGetType {
        NAME, LAST_NAME, FIRST_NAME
    }
}
