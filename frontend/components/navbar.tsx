"use client";

import {
  Navbar as HeroUINavbar,
  NavbarContent,
  NavbarMenu,
  NavbarMenuToggle,
  NavbarBrand,
  NavbarItem,
  NavbarMenuItem,
} from "@heroui/navbar";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";
import { Input } from "@heroui/input";
import {
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
} from "@heroui/dropdown";
import { link as linkStyles } from "@heroui/theme";
import NextLink from "next/link";
import clsx from "clsx";
import { Icon } from "@iconify/react";
import { useEffect } from "react";
import { Kbd } from "@heroui/kbd";
import { useDisclosure } from "@heroui/modal";

import { siteConfig } from "@/config/site";
import { ThemeSwitch } from "@/components/theme-switch";
import { LanguageSwitcher } from "@/components/language-switcher";
import { SearchIcon, Logo } from "@/components/icons";
import { useAuth } from "@/contexts/AuthContext";
import { useTranslations } from "@/hooks/useTranslations";
import { SearchModal } from "@/components/search-modal";

export const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const t = useTranslations();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if ((event.ctrlKey || event.metaKey) && event.key === "k") {
        event.preventDefault();
        onOpen();
      }
    };

    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [onOpen]);

  const searchInput = (
    <>
      <Input
        isReadOnly
        aria-label="Search"
        className="cursor-pointer"
        classNames={{
          inputWrapper: "bg-default-100",
          input: "text-sm",
        }}
        endContent={
          <div className="flex gap-1">
            <Kbd className="hidden lg:inline-block">Ctrl</Kbd>
            <Kbd className="hidden lg:inline-block">K</Kbd>
          </div>
        }
        labelPlacement="outside"
        placeholder={t("nav.search")}
        startContent={
          <SearchIcon className="text-base text-default-400 pointer-events-none flex-shrink-0" />
        }
        type="search"
        onClick={onOpen}
      />
      <SearchModal isOpen={isOpen} onOpenChange={onOpenChange} />
    </>
  );

  return (
    <HeroUINavbar maxWidth="2xl">
      <NavbarContent className="basis-1/5 sm:basis-1/4" justify="start">
        <NavbarBrand as="li" className="gap-3 max-w-fit">
          <NextLink className="flex justify-start items-center gap-1" href="/">
            <Logo />
            <p className="font-bold text-inherit">Protube</p>
          </NextLink>
        </NavbarBrand>
        <ul className="hidden lg:flex gap-4 justify-start ml-2">
          {siteConfig.navItems.map((item) => (
            <NavbarItem key={item.href}>
              <NextLink
                className={clsx(
                  linkStyles({ color: "foreground" }),
                  "data-[active=true]:text-primary data-[active=true]:font-medium",
                )}
                color="foreground"
                href={item.href}
              >
                {t((item as any).labelKey)}
              </NextLink>
            </NavbarItem>
          ))}
        </ul>
      </NavbarContent>

      <NavbarContent className="hidden sm:flex basis-1/2" justify="center">
        <NavbarItem className="hidden lg:flex w-full max-w-xs">
          {searchInput}
        </NavbarItem>
      </NavbarContent>

      <NavbarContent
        className="hidden sm:flex basis-1/4 sm:basis-full"
        justify="end"
      >
        <NavbarItem className="hidden sm:flex gap-2">
          <ThemeSwitch />
        </NavbarItem>
        <NavbarItem className="hidden sm:flex gap-2">
          <LanguageSwitcher />
        </NavbarItem>

        {isAuthenticated && user ? (
          <NavbarItem className="hidden md:flex">
            <Dropdown placement="bottom-end">
              <DropdownTrigger>
                <Button
                  className="text-sm font-normal text-default-600 bg-default-100 flex items-center gap-2"
                  variant="flat"
                >
                  <Icon
                    className="text-xl"
                    icon="heroicons:user-circle-solid"
                  />
                  {user.username}
                </Button>
              </DropdownTrigger>
              <DropdownMenu aria-label="User menu actions">
                <DropdownItem
                  key="info"
                  isReadOnly
                  className="gap-2 cursor-default pointer-events-none"
                  textValue="User Info"
                >
                  <p className="font-semibold">
                    {t("auth.signIn.welcome")} @{user.username}
                  </p>
                </DropdownItem>
                <DropdownItem
                  key="profile"
                  as={NextLink}
                  href={`/profile/${user.username}`}
                  startContent={<Icon icon="heroicons:user" />}
                >
                  {t("nav.myChannel")}
                </DropdownItem>
                <DropdownItem
                  key="logout"
                  color="danger"
                  startContent={
                    <Icon icon="heroicons:arrow-right-on-rectangle" />
                  }
                  textValue={t("nav.logout")}
                  onPress={logout}
                >
                  {t("nav.logout")}
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </NavbarItem>
        ) : (
          <>
            <NavbarItem className="hidden md:flex">
              <Button
                as={NextLink}
                className="text-sm font-normal text-default-600 bg-default-100 flex items-center gap-2"
                href="/login"
                variant="flat"
              >
                <Icon
                  className="text-base"
                  icon="heroicons:arrow-right-on-rectangle"
                />
                {t("nav.signIn")}
              </Button>
            </NavbarItem>
            <NavbarItem className="hidden md:flex">
              <Button
                as={NextLink}
                className="flex items-center gap-2"
                href="/register"
                variant="flat"
              >
                <Icon className="text-base" icon="heroicons:user-plus" />
                {t("nav.register")}
              </Button>
            </NavbarItem>
          </>
        )}
      </NavbarContent>

      <NavbarContent className="sm:hidden basis-1 pl-4" justify="end">
        <LanguageSwitcher />
        <ThemeSwitch />
        <NavbarMenuToggle />
      </NavbarContent>

      <NavbarMenu>
        {searchInput}
        <div className="mx-4 mt-2 flex flex-col gap-2">
          {siteConfig.navItems.map((item, index) => (
            <NavbarMenuItem key={`${item}-${index}`}>
              <Link
                color={
                  index === 2
                    ? "primary"
                    : index === siteConfig.navItems.length - 1
                      ? "danger"
                      : "foreground"
                }
                href={item.href}
                size="lg"
              >
                {t((item as any).labelKey)}
              </Link>
            </NavbarMenuItem>
          ))}

          {/* Auth buttons for mobile */}
          {isAuthenticated && user ? (
            <>
              <NavbarMenuItem>
                <div className="flex flex-col gap-1 py-2">
                  <p className="text-sm font-semibold">{user.username}</p>
                  <p className="text-xs text-default-500">{user.email}</p>
                </div>
              </NavbarMenuItem>
              <NavbarMenuItem>
                <Button
                  className="w-full flex items-center gap-2"
                  color="danger"
                  variant="flat"
                  onPress={logout}
                >
                  <Icon icon="heroicons:arrow-right-on-rectangle" />
                  {t("nav.logout")}
                </Button>
              </NavbarMenuItem>
            </>
          ) : (
            <>
              <NavbarMenuItem>
                <Button
                  as={NextLink}
                  className="w-full flex items-center gap-2"
                  href="/login"
                  variant="flat"
                >
                  <Icon icon="heroicons:arrow-right-on-rectangle" />
                  {t("nav.signIn")}
                </Button>
              </NavbarMenuItem>
              <NavbarMenuItem>
                <Button
                  as={NextLink}
                  className="w-full flex items-center gap-2"
                  href="/register"
                  variant="flat"
                >
                  <Icon icon="heroicons:user-plus" />
                  {t("nav.register")}
                </Button>
              </NavbarMenuItem>
            </>
          )}
        </div>
      </NavbarMenu>
    </HeroUINavbar>
  );
};
