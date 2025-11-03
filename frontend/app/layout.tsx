import "@/styles/globals.css";

import { Providers } from "./providers";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html suppressHydrationWarning lang="en">
        <body>
            <Providers themeProps={{ attribute: "class", defaultTheme: "dark" }}>
                {children}
            </Providers>
        </body>
    </html>
  );
}
