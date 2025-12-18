"use client";

import React, { createContext, useContext, useEffect, useState } from "react";

import { defaultLocale, isValidLocale, Locale } from "@/i18n.config";

interface LocaleContextType {
  locale: Locale;
  setLocale: (locale: Locale) => void;
}

const LocaleContext = createContext<LocaleContextType>({
  locale: defaultLocale,
  setLocale: () => {},
});

export function LocaleProvider({ children }: { children: React.ReactNode }) {
  const [locale, setLocaleState] = useState<Locale>(defaultLocale);
  const [isClient, setIsClient] = useState(false);

  // Initialize locale from localStorage on client side
  useEffect(() => {
    setIsClient(true);
    const storedLocale = localStorage.getItem("locale");

    if (storedLocale && isValidLocale(storedLocale)) {
      setLocaleState(storedLocale);
      document.documentElement.lang = storedLocale;
    } else {
      setLocaleState(defaultLocale);
      document.documentElement.lang = defaultLocale;
    }
  }, []);

  const setLocale = (newLocale: Locale) => {
    setLocaleState(newLocale);
    localStorage.setItem("locale", newLocale);
    // Update document language attribute
    document.documentElement.lang = newLocale;
  };

  return (
    <LocaleContext.Provider value={{ locale, setLocale }}>
      {children}
    </LocaleContext.Provider>
  );
}

export function useLocale() {
  const context = useContext(LocaleContext);

  return context;
}
