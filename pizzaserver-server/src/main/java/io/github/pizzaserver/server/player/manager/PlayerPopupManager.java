package io.github.pizzaserver.server.player.manager;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import com.nukkitx.protocol.bedrock.packet.NpcDialoguePacket;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.dialogue.NPCDialogue;
import io.github.pizzaserver.api.player.dialogue.NPCDialogueResponse;
import io.github.pizzaserver.api.player.form.Form;
import io.github.pizzaserver.api.player.form.ModalForm;
import io.github.pizzaserver.api.player.form.response.FormResponse;
import io.github.pizzaserver.api.player.form.response.ModalFormResponse;
import io.github.pizzaserver.server.player.form.FormUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerPopupManager {

    protected final Player player;
    protected final Map<Integer, Form> openForms = new HashMap<>();
    protected final Map<Integer, Consumer<FormResponse<? extends Form>>> openFormCallbacks = new HashMap<>();
    protected NPCDialogue openDialogue;


    public PlayerPopupManager(Player player) {
        this.player = player;
    }

    public void showForm(Form form, Consumer<FormResponse<? extends Form>> callback) {
        if (this.player.hasSpawned()) {
            this.openForms.put(form.getId(), form);
            this.openFormCallbacks.put(form.getId(), callback);

            ModalFormRequestPacket requestPacket = new ModalFormRequestPacket();
            requestPacket.setFormId(form.getId());
            requestPacket.setFormData(FormUtils.toJSON(form));
            this.player.sendPacket(requestPacket);
        } else {
            switch (form.getType()) {
                case MODAL:
                    callback.accept(new ModalFormResponse.Builder()
                            .setForm((ModalForm) form)
                            .setPlayer(this.player)
                            .setClosed(true)
                            .build());
                    break;
                case CUSTOM:
                    break;
                case SIMPLE:
                    break;
            }
        }
    }

    public void onFormResponse(int formId, String formData) {
        if (this.openForms.containsKey(formId)) {
            Form form = this.openForms.get(formId);
            Consumer<FormResponse<? extends Form>> formCallback = this.openFormCallbacks.get(formId);

            FormResponse<? extends Form> response;
            try {
                response = FormUtils.toResponse(form, this.player, formData);
            } catch (JsonParseException ignored) {
                return;
            } finally {
                this.openForms.remove(formId);
                this.openFormCallbacks.remove(formId);
            }

            formCallback.accept(response);
        }
    }

    public void showDialogue(NPCDialogue dialogue, Consumer<NPCDialogueResponse> callback) {
        boolean shouldShowDialogue = this.player.hasSpawned(); // TODO: check if entity is visible
        if (shouldShowDialogue) {
            this.openDialogue = dialogue;
            // TODO: send metadata packets instead of modifying metadata of entity so that we don't send metadata to all viewers
        } else {
            // TODO: close dialogue response
        }
    }

    public void hideDialogue() {
        if (this.openDialogue != null) {
            NpcDialoguePacket dialoguePacket = new NpcDialoguePacket();
            dialoguePacket.setAction(NpcDialoguePacket.Action.CLOSE);
            this.player.sendPacket(dialoguePacket);
            // TODO: hide dialogue callback
            this.openDialogue = null;
        }
    }

}
