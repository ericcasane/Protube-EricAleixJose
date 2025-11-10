'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@heroui/button';
import { Input } from '@heroui/input';
import { Card, CardBody, CardHeader } from '@heroui/card';
import { Link } from '@heroui/link';
import { useAuth } from '@/contexts/AuthContext';
import { useTranslations } from '@/hooks/useTranslations';
import { title } from '@/components/primitives';

export default function RegisterPage() {
  const router = useRouter();
  const { register } = useAuth();
  const t = useTranslations();

  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [serverError, setServerError] = useState('');

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = t('auth.register.firstNameRequired');
    }

    if (!formData.surname.trim()) {
      newErrors.surname = t('auth.register.lastNameRequired');
    }

    if (!formData.email.trim()) {
      newErrors.email = t('auth.register.emailRequired');
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = t('auth.register.emailInvalid');
    }

    if (!formData.username.trim()) {
      newErrors.username = t('auth.register.usernameRequired');
    }

    if (!formData.password) {
      newErrors.password = t('auth.register.passwordRequired');
    } else if (formData.password.length < 8) {
      newErrors.password = t('auth.register.passwordTooShort');
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = t('auth.register.passwordsDoNotMatch');
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setServerError('');

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      await register({
        name: formData.name,
        surname: formData.surname,
        email: formData.email,
        username: formData.username,
        password: formData.password,
      });

      router.push('/');
    } catch (error) {
      setServerError(
        error instanceof Error ? error.message : t('auth.register.error')
      );
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({ ...prev, [field]: e.target.value }));
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: '' }));
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-200px)] py-12">
      <Card className="w-full max-w-md">
        <CardHeader className="flex flex-col gap-1 items-center pb-6 pt-8">
          <h1 className={title({ size: 'sm' })}>
            {t('auth.register.title')}
            <span aria-label="emoji" className="ml-2" role="img">
            👋
            </span>
          </h1>
          <p className="text-small text-default-500">{t('auth.register.subtitle')}</p>
        </CardHeader>
        <CardBody>
          <form onSubmit={handleSubmit} className="flex flex-col gap-4 px-6">
            <Input
              label={t('auth.register.firstName')}
              placeholder={t('auth.register.firstName')}
              value={formData.name}
              onChange={handleChange('name')}
              isInvalid={!!errors.name}
              errorMessage={errors.name}
              isRequired
              variant="bordered"
            />

            <Input
              label={t('auth.register.lastName')}
              placeholder={t('auth.register.lastName')}
              value={formData.surname}
              onChange={handleChange('surname')}
              isInvalid={!!errors.surname}
              errorMessage={errors.surname}
              isRequired
              variant="bordered"
            />

            <Input
              label={t('auth.register.email')}
              placeholder="example@email.com"
              type="email"
              value={formData.email}
              onChange={handleChange('email')}
              isInvalid={!!errors.email}
              errorMessage={errors.email}
              isRequired
              variant="bordered"
            />

            <Input
              label={t('auth.register.username')}
              placeholder={t('auth.register.username')}
              value={formData.username}
              onChange={handleChange('username')}
              isInvalid={!!errors.username}
              errorMessage={errors.username}
              isRequired
              variant="bordered"
            />

            <Input
              label={t('auth.register.password')}
              placeholder={t('auth.register.password')}
              type="password"
              value={formData.password}
              onChange={handleChange('password')}
              isInvalid={!!errors.password}
              errorMessage={errors.password}
              isRequired
              variant="bordered"
            />

            <Input
              label={t('auth.register.confirmPassword')}
              placeholder={t('auth.register.confirmPassword')}
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange('confirmPassword')}
              isInvalid={!!errors.confirmPassword}
              errorMessage={errors.confirmPassword}
              isRequired
              variant="bordered"
            />

            {serverError && (
              <div className="bg-danger-50 text-danger border border-danger-200 rounded-lg p-3 text-sm">
                {serverError}
              </div>
            )}

            <Button
              type="submit"
              color="primary"
              size="lg"
              isLoading={isLoading}
              className="w-full"
            >
              {t('auth.register.registerButton')}
            </Button>

            <p className="text-center text-small">
              {t('auth.register.haveAccount')}{' '}
              <Link href="/login" size="sm" className="font-semibold">
                {t('auth.register.signIn')}
              </Link>
            </p>
          </form>
        </CardBody>
      </Card>
    </div>
  );
}

