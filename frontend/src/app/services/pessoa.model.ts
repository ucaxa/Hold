export type Sexo = 'M' | 'F';

export interface PessoaRequest {
  nome: string;
  cpf?: string;
  dataNascimento?: string;
  sexo: Sexo;
  altura: number;
  email?: string;
}

export interface PessoaResponse {
  id: number;
  nome: string;
  cpf?: string;
  dataNascimento?: string;
  sexo: Sexo;
  altura: number;
  email?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface PesoIdealResponse {
  pessoaId: number;
  nome: string;
  pesoIdealKg: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}
