import { useAllVideos } from './useAllVideos';

function App() {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <header className="w-full min-h-screen bg-gray-900 flex flex-col items-center justify-center text-white text-center px-4">
        <img
          src="/protube-logo-removebg-preview.png"
          className="h-[40vmin] pointer-events-none"
          alt="logo"
        />
        <ContentApp />
      </header>
    </div>
  );
}

function ContentApp() {
  const { loading, message, value } = useAllVideos();
  switch (loading) {
    case 'loading':
      return <div className="text-lg mt-8">Loading...</div>;
    case 'error':
      return (
        <div className="mt-8 text-center">
          <h3 className="text-2xl font-bold mb-2">Error</h3>
          <p className="text-lg">{message}</p>
        </div>
      );
    case 'success':
      return (
        <div className="mt-8">
          <strong className="text-xl block mb-4">Videos available:</strong>
          <ul className="space-y-2 list-disc list-inside">
            {value.map((item) => (
              <li key={item.videoFileName} className="text-lg">
                {item.title}
              </li>
            ))}
          </ul>
        </div>
      );
  }
  return <div className="text-lg mt-8">Idle...</div>;
}

export default App;
