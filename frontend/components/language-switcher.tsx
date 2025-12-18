"use client";

import React from "react";
import { Select, ListBox } from "@heroui/react";
import { Icon } from "@iconify/react";

import { useLocale } from "@/contexts/LocaleContext";
import { locales, Locale } from "@/i18n.config";

const languageNames: Record<Locale, string> = {
  en: "English",
  es: "Español",
  ca: "Català",
};

export function LanguageSwitcher() {
  const { locale, setLocale } = useLocale();

  return (
    <Select
      aria-label="Select language"
      className="w-auto"
      selectedKey={locale}
      onSelectionChange={(key) => {
        if (key) {
          setLocale(key as Locale);
        }
      }}
    >
      <Select.Trigger>
        <Icon className="text-lg transition-colors" icon="heroicons:language" />
      </Select.Trigger>
      <Select.Content>
        <ListBox>
          {locales.map((lang) => (
            <ListBox.Item
              key={lang}
              className={
                locale === lang
                  ? "dark:bg-neutral-800 border-1 border-neutral-700"
                  : ""
              }
              id={lang}
              textValue={languageNames[lang]}
            >
              {languageNames[lang]}
            </ListBox.Item>
          ))}
        </ListBox>
      </Select.Content>
    </Select>
  );
}
