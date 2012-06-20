<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Publication Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Database ID' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->publication->pub_id;?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Code' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="pub_code" id="pub_code" size="32" maxlength="250" value="<?php echo $this->publication->pub_code;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Name' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="pub_name" id="pub_name" size="32" maxlength="250" value="<?php echo $this->publication->pub_name;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Principal' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="pub_principal" id="pub_principal" size="32" maxlength="250" value="<?php echo $this->publication->pub_principal;?>" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Category' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A CATEGORY'));
    //dump($this->categories, "CATS");
    foreach($this->categories as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->cat_code, JText::_($value->cat_name));
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'pub_category', 'class="inputbox"', 'value', 'text', $this->publication->pub_category);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Number of Issues' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="pub_numissues" id="pub_numissues" size="32" maxlength="250" value="<?php echo $this->publication->pub_numissues;?>" />
			</td>
		</tr>		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Frequency' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$optionsFreq = array(JHTML::_('select.option','',JText::_('PLS CHOOSE FREQUENCY')),
				  JHTML::_('select.option', 'Bi monthly', JText::_('Bi monthly')),
				  JHTML::_('select.option', 'Bi Weekly', JText::_('Bi Weekly')),
				  JHTML::_('select.option', 'Half Yearly', JText::_('Half Yearly')),
				  JHTML::_('select.option', 'Monthly', JText::_('Monthly')),
				  JHTML::_('select.option', 'Quarterly', JText::_('Quarterly')),
				  JHTML::_('select.option', 'Weekly', JText::_('Weekly')),
				  JHTML::_('select.option', 'Daily', JText::_('Daily')),
				  JHTML::_('select.option', 'Yearly', JText::_('Yearly'))
				);
    echo JHTML::_('select.genericlist', $optionsFreq, 'pub_frequency', 'class="inputbox"', 'value', 'text', $this->publication->pub_frequency);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Currency' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$optionsCurrency = array(JHTML::_('select.option','',JText::_('PLS CHOOSE CURRENCY')),
				  JHTML::_('select.option', 'Euro', JText::_('Euro')),
				  JHTML::_('select.option', 'GBP', JText::_('GBP')),
				  JHTML::_('select.option', 'USD', JText::_('USD')),
				  JHTML::_('select.option', 'NPR', JText::_('NPR')),
				  JHTML::_('select.option', 'INR', JText::_('INR'))				  
				);
    echo JHTML::_('select.genericlist', $optionsCurrency, 'pub_currency', 'class="inputbox"', 'value', 'text', $this->publication->pub_currency);
				?>
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Rate 1' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="pub_rate1" id="pub_rate1" size="32" maxlength="250" value="<?php echo $this->publication->pub_rate1;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Rate 2' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="pub_rate2" id="pub_rate2" size="32" maxlength="250" value="<?php echo $this->publication->pub_rate2;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Rate 3' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="pub_rate3" id="pub_rate3" size="32" maxlength="250" value="<?php echo $this->publication->pub_rate3;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Rate 4' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="pub_rate4" id="pub_rate4" size="32" maxlength="250" value="<?php echo $this->publication->pub_rate4;?>" />
			</td>
		</tr>
	  <tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Created Date| Modified Date' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->publication->cdate;?>|<?php echo $this->publication->mdate;?>
			</td>
		</tr>
		
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="pub_id" value="<?php echo $this->publication->pub_id; ?>" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="publication" />
</form>
