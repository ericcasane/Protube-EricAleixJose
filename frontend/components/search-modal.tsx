"use client";

import { useEffect, useState } from "react";
import { Modal, ModalContent, ModalBody, ModalHeader } from "@heroui/modal";
import { Input } from "@heroui/input";
import { Spinner } from "@heroui/spinner";
import { Kbd } from "@heroui/kbd";
import { motion, AnimatePresence } from "framer-motion";
import Image from "next/image";
import NextLink from "next/link";

import { SearchIcon } from "@/components/icons";
import { useDebounce } from "@/hooks/useDebounce";
import { Video, Page } from "@/types/video";
import { useTranslations } from "@/hooks/useTranslations";

interface SearchModalProps {
  isOpen: boolean;
  onOpenChange: () => void;
}

export const SearchModal = ({ isOpen, onOpenChange }: SearchModalProps) => {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<Video[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const debouncedQuery = useDebounce(query, 300);
  const t = useTranslations();

  useEffect(() => {
    const searchVideos = async () => {
      if (!debouncedQuery.trim()) {
        setResults([]);

        return;
      }

      setIsLoading(true);
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"}/api/videos/search?query=${encodeURIComponent(debouncedQuery)}&size=5`,
        );

        if (response.ok) {
          const data: Page<Video> = await response.json();

          setResults(data.content);
        } else {
          console.error("Search failed");
        }
      } catch (error) {
        console.error("Search error:", error);
      } finally {
        setIsLoading(false);
      }
    };

    searchVideos();
  }, [debouncedQuery]);

  const handleClose = () => {
    setQuery("");
    setResults([]);
    onOpenChange();
  };

  return (
    <Modal
      hideCloseButton
      backdrop="blur"
      classNames={{
        base: "bg-background/80 backdrop-blur-md border border-default-200 mt-16",
        header: `p-4 ${results.length > 0 || isLoading ? "border-b border-default-200" : ""}`,
        body: "p-0",
        wrapper: "items-start",
      }}
      isOpen={isOpen}
      motionProps={{
        variants: {
          enter: {
            y: 0,
            opacity: 1,
            transition: {
              duration: 0.3,
              ease: "easeOut",
            },
          },
          exit: {
            y: -20,
            opacity: 0,
            transition: {
              duration: 0.2,
              ease: "easeIn",
            },
          },
        },
      }}
      placement="top"
      scrollBehavior="inside"
      size="2xl"
      onOpenChange={handleClose}
    >
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">
              <Input
                autoFocus
                classNames={{
                  base: "max-w-full sm:max-w-[100%]",
                  mainWrapper: "h-full",
                  input: "text-small",
                  inputWrapper:
                    "h-full font-normal text-default-500 bg-default-400/20 dark:bg-default-500/20",
                }}
                endContent={<Kbd>ESC</Kbd>}
                placeholder={t("nav.search")}
                size="lg"
                startContent={<SearchIcon size={18} />}
                value={query}
                variant="flat"
                onValueChange={setQuery}
              />
            </ModalHeader>
            {(results.length > 0 ||
              isLoading ||
              (debouncedQuery && results.length === 0)) && (
              <ModalBody className="pb-4 px-2 max-h-[60vh] overflow-y-auto custom-scrollbar">
                {isLoading ? (
                  <div className="flex justify-center items-center py-8">
                    <Spinner size="lg" />
                  </div>
                ) : (
                  <div className="flex flex-col gap-2">
                    <AnimatePresence>
                      {results.map((video) => (
                        <motion.div
                          key={video.id}
                          animate={{ opacity: 1, y: 0 }}
                          exit={{ opacity: 0, y: -10 }}
                          initial={{ opacity: 0, y: 10 }}
                          transition={{ duration: 0.2 }}
                        >
                          <NextLink
                            className="flex items-center gap-4 p-2 rounded-lg hover:bg-default-100 transition-colors group"
                            href={`/video/${video.id}`}
                            onClick={onClose}
                          >
                            <div className="relative w-32 aspect-video rounded-md overflow-hidden flex-shrink-0">
                              <Image
                                fill
                                alt={video.title}
                                className="object-cover transition-transform duration-300 group-hover:scale-110"
                                src={`/media/${video.thumbnailUrl}`}
                              />
                            </div>
                            <div className="flex flex-col gap-1 flex-grow min-w-0">
                              <h4 className="text-sm font-semibold truncate group-hover:text-primary transition-colors">
                                {video.title}
                              </h4>
                              <p className="text-xs text-default-500 truncate">
                                {video.channelName}
                              </p>
                            </div>
                          </NextLink>
                        </motion.div>
                      ))}
                    </AnimatePresence>
                    {debouncedQuery && results.length === 0 && !isLoading && (
                      <div className="text-center py-8 text-default-500">
                        No results found for "{debouncedQuery}"
                      </div>
                    )}
                  </div>
                )}
              </ModalBody>
            )}
          </>
        )}
      </ModalContent>
    </Modal>
  );
};
