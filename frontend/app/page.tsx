import { title } from "@/components/primitives";

export default function Home() {
  return (
    <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
      <div className="inline-block max-w-lg text-center justify-center">
        <span className={title()}>The&nbsp;</span>
        <span className={title({ color: "blue" })}>ultimate&nbsp;</span>
        <br />
        <span className={title()}>
          platform for video enthusiasts.
        </span>
      </div>


      <div className="mt-8">

      </div>
    </section>
  );
}
