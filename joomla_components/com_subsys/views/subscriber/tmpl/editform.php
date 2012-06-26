<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Subscriber Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Database ID' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->subscriber->sub_id;?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Code' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_code" id="sub_code" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_code;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Name' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_name" id="sub_name" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_name;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Category' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_category" id="sub_category" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_category;?>" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Address' ); ?>:
				</label>
			</td>
			<td>
				<input class="text_area" type="text" name="sub_address" id="sub_address" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_address;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'City' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_city" id="sub_city" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_city;?>" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'PO Box' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_pobox" id="sub_pobox" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_pobox;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Phone' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_phone" id="sub_phone" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_phone;?>" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Fax' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_fax" id="sub_fax" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_fax;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Email' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_email" id="sub_email" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_email;?>" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Website' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_web" id="sub_web" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_web;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Customer ID' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="customer_id" id="customer_id" size="32" maxlength="250" value="<?php echo $this->subscriber->customer_id;?>" />
			</td>			
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Contact Person' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_cp" id="sub_cp" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_cp;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>			
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Contact Person Designation' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="sub_cpd" id="sub_cpd" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_cpd;?>" />
			</td>			
		</tr>
		<tr>
		<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Created Date| Modified Date' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->subscriber->cdate;?>|<?php echo $this->subscriber->mdate;?>
			</td>
			</tr>
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="sub_id" value="<?php echo $this->subscriber->sub_id; ?>" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="subscriber" />
</form>
