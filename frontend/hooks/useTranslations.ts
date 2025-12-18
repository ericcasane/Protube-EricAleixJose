"use client";

import { useMemo } from "react";

import { useLocale } from "@/contexts/LocaleContext";
import enMessages from "@/messages/en.json";
import esMessages from "@/messages/es.json";
import caMessages from "@/messages/ca.json";

type Messages = Record<string, any>;

const translations: Record<string, Messages> = {
  en: enMessages,
  es: esMessages,
  ca: caMessages,
};

export function useTranslations(namespace?: string) {
  const { locale } = useLocale();

  return useMemo(() => {
    const messages = translations[locale] || translations["en"];

    const t = (key: string, options?: Record<string, string | number>) => {
      let value = messages;
      const keyPath = namespace ? `${namespace}.${key}` : key;

      // Navigate through nested object
      for (const part of keyPath.split(".")) {
        value = value?.[part];
      }

      if (typeof value !== "string") {
        return key;
      }

      // Replace placeholders like {count}
      if (options) {
        let result = value as string;

        for (const [placeholder, replacement] of Object.entries(options)) {
          result = result.split(`{${placeholder}}`).join(String(replacement));
        }

        return result;
      }

      return value;
    };

    return t;
  }, [locale, namespace]);
}
