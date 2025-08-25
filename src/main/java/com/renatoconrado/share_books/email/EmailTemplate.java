package com.renatoconrado.share_books.email;

public class EmailTemplate {

    public enum Name {
        CONFIRM_EMAIL("confirm-email"),
        ACTIVATE_ACCOUNT("activate_account");

        private final String content;

        Name(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return this.content;
        }
    }
}
