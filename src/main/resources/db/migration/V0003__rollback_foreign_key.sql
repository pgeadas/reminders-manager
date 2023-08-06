alter table if exists occurrences
    add constraint fk_reminders foreign key (reminder_id) references reminders (id);


