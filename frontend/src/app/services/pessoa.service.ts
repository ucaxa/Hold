import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse, PessoaRequest, PessoaResponse, PesoIdealResponse } from './pessoa.model';

const API = '/api';

@Injectable({ providedIn: 'root' })
export class PessoaService {
  constructor(private http: HttpClient) {}

  pesquisar(nome?: string): Observable<PessoaResponse[]> {
    let params = new HttpParams();
    if (nome != null && nome.trim() !== '') {
      params = params.set('nome', nome.trim());
    }
    return this.http.get<PessoaResponse[]>(`${API}/pessoas`, { params });
  }

  listarPaginado(page: number = 0, size: number = 5): Observable<PageResponse<PessoaResponse>> {
    const params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size));
    return this.http.get<PageResponse<PessoaResponse>>(`${API}/pessoas/paginado`, { params });
  }

  buscarPorId(id: number): Observable<PessoaResponse> {
    return this.http.get<PessoaResponse>(`${API}/pessoas/${id}`);
  }

  incluir(request: PessoaRequest): Observable<PessoaResponse> {
    return this.http.post<PessoaResponse>(`${API}/pessoas`, request);
  }

  alterar(id: number, request: PessoaRequest): Observable<PessoaResponse> {
    return this.http.put<PessoaResponse>(`${API}/pessoas/${id}`, request);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/pessoas/${id}`);
  }

  calcularPesoIdeal(id: number): Observable<PesoIdealResponse> {
    return this.http.get<PesoIdealResponse>(`${API}/pessoas/${id}/peso-ideal`);
  }
}
