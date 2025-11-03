import { Snippet } from "@heroui/snippet";
import { Code } from "@heroui/code";
import {Button} from "@heroui/button";

export default function Home() {
  return (
    <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
      <div className="inline-block max-w-xl text-center justify-center">
        <span>Make&nbsp;</span>
        <span>beautiful&nbsp;</span>
        <br />
        <span>
          websites regardless of your design experience.
        </span>
        <div>
          Beautiful, fast and modern React UI library.
        </div>
          <Button color="primary">Button</Button>
      </div>


      <div className="mt-8">
        <Snippet hideCopyButton hideSymbol variant="bordered">
          <span>
            Get started by editing <Code color="primary">app/page.tsx</Code>
          </span>
        </Snippet>
      </div>
    </section>
  );
}
