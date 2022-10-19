import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SolicitacaoDetailComponent } from './solicitacao-detail.component';

describe('Solicitacao Management Detail Component', () => {
  let comp: SolicitacaoDetailComponent;
  let fixture: ComponentFixture<SolicitacaoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SolicitacaoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ solicitacao: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SolicitacaoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SolicitacaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load solicitacao on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.solicitacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
