/// <reference types="cypress" />

context('Calc', () => {
  beforeEach(() => {
    cy.visit('http://calc-web/')
  })

  it('get the title', () => {
    cy.title().should('include', 'Calculator')
  })

  it('can type operands', () => {
    cy.get('#in-op1').clear().should('have.value', '')
      .type('5').should('have.value', '5')
    cy.get('#in-op2').clear().should('have.value', '')
      .type('-5').should('have.value', '-5')
  })

  it('can click add', () => {
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-add').click()
    cy.get('#result-area').should('have.text', "Result: 5")
    cy.screenshot()
  })

  it('can click multiply', () => {
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-multiply').click()
    cy.get('#result-area').should('have.text', "Result: 6")
    cy.screenshot()
  })

  it('can click substract (using fixture)', () => {
    cy.fixture('result8.txt').as('result')
    cy.server()
    cy.route('GET', 'calc/substract/4/-4', '@result').as('getResult')

    cy.get('#in-op1').clear().type('4')
    cy.get('#in-op2').clear().type('-4')
    cy.get('#button-substract').click()

    cy.wait('@getResult')

    cy.get('#result-area').should('have.text', "Result: 8")
    cy.screenshot()
  })

  it('increases the history log', () => {
    cy.get('#button-add').click().click().click()
    cy.get('#history-log').children().its('length')
    .should('eq', 3)
    cy.screenshot()
  })

  it('can click divide', () => {
    cy.get('#in-op1').clear().type('6')
    cy.get('#in-op2').clear().type('2')
    cy.get('#button-divide').click()
    cy.get('#result-area').should('have.text', "Result: 3")
    cy.screenshot()
  })

  it('can click power', () => {
    cy.get('#in-op1').clear().type('2')
    cy.get('#in-op2').clear().type('3')
    cy.get('#button-power').click()
    cy.get('#result-area').should('have.text', "Result: 8")
    cy.screenshot()
  })

  it('can click sqrt', () => {
    cy.get('#in-op1').clear().type('16')
    cy.get('#button-sqrt').click()
    cy.get('#result-area').should('have.text', "Result: 4")
    cy.screenshot()
  })

  it('can click log', () => {
    cy.get('#in-op1').clear().type('1')
    cy.get('#button-log').click()
    cy.get('#result-area').should('have.text', "Result: 0")
    cy.screenshot()
  })
})
