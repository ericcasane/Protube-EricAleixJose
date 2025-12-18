/** @type {import('next').NextConfig} */
const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
const backendUrlParsed = new URL(backendUrl);

/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: backendUrlParsed.protocol.replace(':', ''),
        hostname: backendUrlParsed.hostname,
        port: backendUrlParsed.port,
        pathname: '/media/**',
      },
    ],
  },
  async rewrites() {
    return [
      {
        source: '/media/:path*',
        destination: `${backendUrl}/media/:path*`, // proxy al backend
      },
    ];
  },
  eslint: {
    ignoreDuringBuilds: true,
  },
};

module.exports = nextConfig;