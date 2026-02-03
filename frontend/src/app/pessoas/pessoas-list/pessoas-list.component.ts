import { Component, signal, computed, effect, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PessoaService } from '../../services/pessoa.service';
import { PessoaRequest, PessoaResponse, PesoIdealResponse, Sexo } from '../../services/pessoa.model';

@Component({
  selector: 'app-pessoas-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pessoas-list.component.html',
  styleUrl: './pessoas-list.component.css',
})
export class PessoasListComponent implements OnInit {
  pessoas = signal<PessoaResponse[]>([]);
  selected = signal<PessoaResponse | null>(null);
  searchTerm = signal('');
  loading = signal(false);
  error = signal<string | null>(null);
  page = signal(0);
  pageSize = signal(5);
  totalElements = signal(0);
  totalPages = signal(0);
  useSearch = signal(false);
  formOpen = signal(false);
  editing = signal<PessoaResponse | null>(null);
  pesoIdealResult = signal<PesoIdealResponse | null>(null);
  pesoIdealOpen = signal(false);
  pesoIdealLoading = signal(false);

  formData = signal<PessoaRequest>({
    nome: '',
    cpf: '',
    dataNascimento: '',
    sexo: 'M',
    altura: 0,
    email: '',
  });
  formErrors = signal<Record<string, string>>({});
  successMessage = signal<string | null>(null);
  /** Indica se o usuário já clicou em Pesquisar; Alterar e Excluir só habilitam após pesquisa. */
  searchPerformed = signal(false);

  selectedPessoa = computed(() => this.selected());
  /** Alterar, Excluir e Peso Ideal habilitam quando a pesquisa foi feita e encontrou registros. */
  canAlterOrExclude = computed(() => this.searchPerformed() && this.pessoas().length > 0);

  pageNumbers = computed(() => {
    const total = this.totalPages();
    const current = this.page();
    const delta = 2;
    const start = Math.max(0, current - delta);
    const end = Math.min(total, current + delta + 1);
    const pages: number[] = [];
    for (let i = start; i < end; i++) pages.push(i);
    return pages;
  });

  constructor(private pessoaService: PessoaService) {
    effect(() => {
      const p = this.editing();
      if (p) {
        this.formData.set({
          nome: p.nome,
          cpf: this.formatCpf(p.cpf ?? ''),
          dataNascimento: p.dataNascimento ?? '',
          sexo: p.sexo,
          altura: p.altura,
          email: p.email ?? '',
        });
      } else {
        this.formData.set({
          nome: '',
          cpf: '',
          dataNascimento: '',
          sexo: 'M',
          altura: 0,
          email: '',
        });
      }
    });
  }

  ngOnInit(): void {
    this.loadPessoas();
  }

  loadPessoas(): void {
    this.loading.set(true);
    this.error.set(null);
    const term = this.searchTerm().trim();
    if (term) {
      this.useSearch.set(true);
      this.pessoaService.pesquisar(term).subscribe({
        next: (list) => {
          this.pessoas.set(list);
          this.totalElements.set(list.length);
          this.totalPages.set(1);
          this.loading.set(false);
          this.updateSelectionAfterLoad(list);
        },
        error: (err) => {
          this.error.set(err?.message ?? 'Erro ao carregar pessoas');
          this.loading.set(false);
        },
      });
    } else {
      this.useSearch.set(false);
      this.pessoaService.listarPaginado(this.page(), this.pageSize()).subscribe({
        next: (res) => {
          this.pessoas.set(res.content);
          this.totalElements.set(res.totalElements);
          this.totalPages.set(res.totalPages);
          this.loading.set(false);
          this.updateSelectionAfterLoad(res.content);
        },
        error: (err) => {
          this.error.set(err?.message ?? 'Erro ao carregar pessoas');
          this.loading.set(false);
        },
      });
    }
  }

  onSearchTermChange(value: string): void {
    this.searchTerm.set(value);
    if (value.trim() === '') {
      this.page.set(0);
      this.loadPessoas();
    }
  }

  onPesquisar(): void {
    this.page.set(0);
    this.searchPerformed.set(true);
    this.loadPessoas();
  }

  /**
   * Após carregar a lista: se retornou 1 registro, seleciona automaticamente (Alterar/Excluir sem clicar na linha);
   * se retornou 0 ou mais de 1, limpa a seleção (com vários, o usuário deve clicar no registro desejado).
   */
  private updateSelectionAfterLoad(list: PessoaResponse[]): void {
    if (list.length === 1) {
      this.selected.set(list[0]);
    } else {
      this.selected.set(null);
    }
  }

  goToPage(p: number): void {
    if (p < 0 || p >= this.totalPages()) return;
    this.page.set(p);
    this.loadPessoas();
  }

  selectRow(p: PessoaResponse): void {
    this.selected.set(p);
  }

  /** Abre o formulário para incluir uma nova pessoa (sem exigir seleção). */
  openFormForNew(): void {
    this.editing.set(null);
    this.formData.set({
      nome: '',
      cpf: '',
      dataNascimento: '',
      sexo: 'M',
      altura: 0,
      email: '',
    });
    this.formErrors.set({});
    this.formOpen.set(true);
  }

  /** Abre o formulário para alterar a pessoa passada ou a selecionada na tabela. */
  openForm(p?: PessoaResponse): void {
    if (!p && !this.selected()) {
      alert('Selecione uma pessoa na tabela para alterar.');
      return;
    }
    this.editing.set(p ?? this.selected());
    this.formErrors.set({});
    this.formOpen.set(true);
  }

  closeForm(): void {
    this.formOpen.set(false);
    this.editing.set(null);
  }

  onFormSubmit(): void {
    const data = this.formData();
    const err: Record<string, string> = {};
    if (!data.nome?.trim()) err['nome'] = 'Nome é obrigatório';
    if (data.sexo == null) err['sexo'] = 'Sexo é obrigatório';
    if (data.altura == null || data.altura <= 0) err['altura'] = 'Altura é obrigatória e deve ser maior que zero';
    const cpfTrim = data.cpf?.trim();
    if (cpfTrim && !this.validarCpf(cpfTrim)) err['cpf'] = 'CPF inválido';
    const emailTrim = data.email?.trim();
    if (emailTrim && !this.validarEmail(emailTrim)) err['email'] = 'E-mail inválido';
    this.formErrors.set(err);
    if (Object.keys(err).length > 0) return;

    const req: PessoaRequest = {
      nome: data.nome.trim(),
      cpf: data.cpf?.trim() || undefined,
      dataNascimento: data.dataNascimento?.trim() || undefined,
      sexo: data.sexo,
      altura: Number(data.altura),
      email: data.email?.trim() || undefined,
    };

    const id = this.editing()?.id;
    if (id != null) {
      this.pessoaService.alterar(id, req).subscribe({
        next: () => {
          this.closeForm();
          this.loadPessoas();
          this.selected.set(null);
          this.showSuccess();
        },
        error: (e) => this.setFormErrorsFromResponse(e),
      });
    } else {
      this.pessoaService.incluir(req).subscribe({
        next: () => {
          this.closeForm();
          this.loadPessoas();
          this.showSuccess();
        },
        error: (e) => this.setFormErrorsFromResponse(e),
      });
    }
  }

  private setFormErrorsFromResponse(e: { status?: number; error?: Record<string, string> | { message?: string } }): void {
    if (e?.status === 400 && e?.error && typeof e.error === 'object') {
      const map = e.error as Record<string, string>;
      if (map['cpf'] != null || map['nome'] != null || map['altura'] != null || map['sexo'] != null || map['email'] != null) {
        this.formErrors.set(map);
        return;
      }
    }
    const msg = (e?.error as { message?: string })?.message ?? 'Erro ao salvar';
    this.formErrors.set({ submit: msg });
  }

  private showSuccess(): void {
    this.successMessage.set('Pessoa Salva com Sucesso');
    setTimeout(() => this.successMessage.set(null), 4000);
  }

  formatCpf(value: string): string {
    const digits = value.replace(/\D/g, '').slice(0, 11);
    if (digits.length <= 3) return digits;
    if (digits.length <= 6) return `${digits.slice(0, 3)}.${digits.slice(3)}`;
    if (digits.length <= 9) return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`;
  }

  onCpfInput(value: string): void {
    this.updateFormField('cpf', this.formatCpf(value));
    if (this.formErrors()['cpf']) {
      this.formErrors.update((err) => {
        const next = { ...err };
        delete next['cpf'];
        return next;
      });
    }
  }

  validarCpf(cpf: string): boolean {
    const digits = cpf.replace(/\D/g, '');
    if (digits.length !== 11) return false;
    if (/^(\d)\1{10}$/.test(digits)) return false;
    const calcDigit = (base: string, weights: number[]): number => {
      let sum = 0;
      for (let i = 0; i < base.length; i++) sum += parseInt(base[i], 10) * weights[i];
      const rem = sum % 11;
      return rem < 2 ? 0 : 11 - rem;
    };
    const d1 = calcDigit(digits.slice(0, 9), [10, 9, 8, 7, 6, 5, 4, 3, 2]);
    const d2 = calcDigit(digits.slice(0, 9) + d1, [11, 10, 9, 8, 7, 6, 5, 4, 3, 2]);
    return d1 === parseInt(digits[9], 10) && d2 === parseInt(digits[10], 10);
  }

  validarEmail(email: string): boolean {
    if (!email?.trim()) return true;
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email.trim());
  }

  onEmailInput(value: string): void {
    this.updateFormField('email', value);
    if (this.formErrors()['email']) {
      this.formErrors.update((err) => {
        const next = { ...err };
        delete next['email'];
        return next;
      });
    }
  }

  onExcluir(): void {
    const p = this.selected();
    if (!p) {
      alert('Selecione uma pessoa na tabela para excluir.');
      return;
    }
    if (!confirm('Tem certeza que deseja excluir esta pessoa?')) return;
    this.pessoaService.excluir(p.id).subscribe({
      next: () => {
        this.selected.set(null);
        this.searchTerm.set('');
        this.page.set(0);
        this.loadPessoas();
        this.successMessage.set('Registro excluído com sucesso');
        setTimeout(() => this.successMessage.set(null), 4000);
      },
      error: (e) => alert('Erro ao excluir: ' + (e?.error?.message ?? e?.message)),
    });
  }

  openPesoIdeal(): void {
    const p = this.selected();
    if (!p) {
      alert('Selecione uma pessoa na tabela (clique em Pesquisar e depois em uma linha).');
      return;
    }
    this.pesoIdealOpen.set(true);
    this.pesoIdealLoading.set(true);
    this.pesoIdealResult.set(null);
    this.pessoaService.calcularPesoIdeal(p.id).subscribe({
      next: (res) => {
        this.pesoIdealResult.set(res);
        this.pesoIdealLoading.set(false);
      },
      error: () => {
        this.pesoIdealLoading.set(false);
        this.pesoIdealResult.set(null);
        alert('Erro ao calcular peso ideal.');
      },
    });
  }

  closePesoIdeal(): void {
    this.pesoIdealOpen.set(false);
    this.pesoIdealResult.set(null);
  }

  updateFormField<K extends keyof PessoaRequest>(key: K, value: PessoaRequest[K]): void {
    this.formData.update((d) => ({ ...d, [key]: value }));
  }

  formatDate(s?: string): string {
    if (!s) return '-';
    try {
      return new Date(s).toLocaleDateString('pt-BR');
    } catch {
      return s;
    }
  }

  sexoLabel(s: Sexo): string {
    return s === 'M' ? 'Masculino' : 'Feminino';
  }

  endItem(): number {
    return Math.min((this.page() + 1) * this.pageSize(), this.totalElements());
  }
}
