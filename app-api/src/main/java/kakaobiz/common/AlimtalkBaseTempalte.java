package kakaobiz.common;

import kakaobiz.template.AlimtalkRecipients;

public abstract class AlimtalkBaseTempalte {
    String recipientNo;

    public AlimtalkBaseTempalte(String recipientNo) {
        this.recipientNo = recipientNo;
    }

    public AlimtalkRecipients getAlimtalkRecipients() {
        return new AlimtalkRecipients(recipientNo, getTemplateParameter());
    }

    protected abstract Object getTemplateParameter();
}
