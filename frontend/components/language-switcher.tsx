'use client';

import React from 'react';
import { Select, ListBox } from '@heroui/react';
import { Icon } from '@iconify/react';
import { useLocale } from '@/contexts/LocaleContext';
import { locales, Locale } from '@/i18n.config';

const languageNames: Record<Locale, string> = {
  en: 'English',
  es: 'Español',
  ca: 'Català',
};

export function LanguageSwitcher() {
  const { locale, setLocale } = useLocale();

  return (
    <Select
      selectedKey={locale}
      onSelectionChange={(key) => {
        if (key) {
          setLocale(key as Locale);
        }
      }}
      aria-label="Select language"
      className="w-auto"
    >
      <Select.Trigger>
        <Icon icon="heroicons:language" className="text-lg transition-colors" />
      </Select.Trigger>
      <Select.Content>
        <ListBox>
          {locales.map((lang) => (
            <ListBox.Item
              key={lang}
              id={lang}
              textValue={languageNames[lang]}
              className={locale === lang ? 'dark:bg-neutral-800 border-1 border-neutral-700' : ''}
            >
              {languageNames[lang]}
            </ListBox.Item>
          ))}
        </ListBox>
      </Select.Content>
    </Select>
  );
}
