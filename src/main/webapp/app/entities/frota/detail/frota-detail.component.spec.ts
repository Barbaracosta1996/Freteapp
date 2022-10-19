import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FrotaDetailComponent } from './frota-detail.component';

describe('Frota Management Detail Component', () => {
  let comp: FrotaDetailComponent;
  let fixture: ComponentFixture<FrotaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FrotaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ frota: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FrotaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FrotaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load frota on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.frota).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
