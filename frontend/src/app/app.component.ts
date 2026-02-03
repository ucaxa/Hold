import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="h-screen flex flex-col bg-gray-50 overflow-hidden">
      <header class="flex-shrink-0">
        <div class="bg-gradient-to-r from-blue-600 to-blue-700 text-white px-6 py-5 shadow-md">
          <div class="max-w-7xl mx-auto flex items-center justify-center">
            <h1 class="text-2xl font-bold tracking-tight">Hold - Gestão de Pessoas</h1>
          </div>
        </div>
      </header>

      <main class="flex-1 min-h-0 overflow-auto">
        <router-outlet />
      </main>

      <footer class="flex-shrink-0 bg-white border-t py-1">
        <p class="text-center text-gray-500 text-xs">Hold - Global Tech Holding</p>
      </footer>
    </div>
  `,
})
export class AppComponent {
  title = 'frontend';
}
