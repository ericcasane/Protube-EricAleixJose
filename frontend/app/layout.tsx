import "@/styles/globals.css";

import { Providers } from "./providers";
import { Navbar } from "@/components/navbar";
import clsx from "clsx";
import { fontSans } from "@/styles/fonts";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html suppressHydrationWarning lang="en">
        <body
          className={clsx(
            "min-h-screen text-foreground bg-background font-sans antialiased",
            fontSans.variable,
          )}
        >
            <Providers themeProps={{ attribute: "class", defaultTheme: "dark" }}>
              <div className="relative flex flex-col">
                <Navbar />
                <main className="container mx-auto max-w-7xl pt-16 px-6 flex-grow">
                  {children}
                </main>
              </div>
            </Providers>
        </body>
    </html>
  );
}
