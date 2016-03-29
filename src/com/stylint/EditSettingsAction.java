package com.stylint;

import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.icons.AllIcons.General;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.stylint.settings.StylintSettingsPage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class EditSettingsAction implements IntentionAction, Iconable, HighPriorityAction {
    private static boolean ourInvoked;
    private final boolean fileLevelAnnotation;
    private final Icon icon;
    private final StylintSettingsPage configurable;

    EditSettingsAction(@NotNull StylintSettingsPage configurable) {
        this(configurable, false, General.Settings);
    }

    private EditSettingsAction(@NotNull StylintSettingsPage configurable, boolean fileLevelAnnotation, @NotNull Icon icon) {
        this.configurable = configurable;
        this.fileLevelAnnotation = fileLevelAnnotation;
        this.icon = icon;
    }

    public Icon getIcon(@IconFlags int flags) {
        return this.icon;
    }

    @NotNull
    public String getText() {
        return this.fileLevelAnnotation ? "Settings..." : this.configurable.getDisplayName() + " settings...";
    }

    @NotNull
    public String getFamilyName() {
        return this.getText();
    }

    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return true;
    }

    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        if (!ourInvoked) {
            ourInvoked = true;
            ApplicationManager.getApplication().invokeLater(() -> {
                EditSettingsAction.ourInvoked = false;
                EditSettingsAction.this.configurable.showSettings();
            }, ModalityState.any());
        }
    }

    public boolean startInWriteAction() {
        return false;
    }
}
