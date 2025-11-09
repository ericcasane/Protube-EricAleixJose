'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@heroui/button';
import { Input } from '@heroui/input';
import { Card, CardBody, CardHeader } from '@heroui/card';
import { Link } from '@heroui/link';
import { useAuth } from '@/contexts/AuthContext';
import { title } from '@/components/primitives';

export default function LoginPage() {
  const router = useRouter();
  const { login } = useAuth();

  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [serverError, setServerError] = useState('');

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.username.trim()) {
      newErrors.username = 'El nom d\'usuari és obligatori';
    }

    if (!formData.password) {
      newErrors.password = 'La contrasenya és obligatòria';
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
      await login({
        username: formData.username,
        password: formData.password,
      });

      router.push('/');
    } catch (error) {
      setServerError(
        error instanceof Error
          ? error.message
          : 'Error en l\'inici de sessió. Comprova les teves credencials.'
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
    if (serverError) {
      setServerError('');
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-200px)] py-12">
      <Card className="w-full max-w-md">
        <CardHeader className="flex flex-col gap-1 items-center pb-6 pt-8">
          <h1 className={title({ size: 'sm' })}>
            Benvingut a <span className={title({ color: 'blue', size: 'sm' })}>ProTube</span>
          </h1>
          <p className="text-small text-default-500">Inicia sessió per continuar</p>
        </CardHeader>
        <CardBody>
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <Input
              label="Nom d'usuari"
              placeholder="Introdueix el teu nom d'usuari"
              value={formData.username}
              onChange={handleChange('username')}
              isInvalid={!!errors.username}
              errorMessage={errors.username}
              isRequired
              variant="bordered"
              autoComplete="username"
            />

            <Input
              label="Contrasenya"
              placeholder="Introdueix la teva contrasenya"
              type="password"
              value={formData.password}
              onChange={handleChange('password')}
              isInvalid={!!errors.password}
              errorMessage={errors.password}
              isRequired
              variant="bordered"
              autoComplete="current-password"
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
              Iniciar sessió
            </Button>

            <p className="text-center text-small">
              No tens compte?{' '}
              <Link href="/register" size="sm" className="font-semibold">
                Registra't ara
              </Link>
            </p>
          </form>
        </CardBody>
      </Card>
    </div>
  );
}

